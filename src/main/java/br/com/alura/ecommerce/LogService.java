package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) {

        var consumer = new KafkaConsumer<String, String>(properties());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:ms");

        consumer.subscribe(Pattern.compile("ECOMMERCE.*")); // Se inscreve em qualquer coisa que tenha ECOMMERCE no começo
       while(true) {
           var records = consumer.poll(Duration.ofMillis(100));
           if (!records.isEmpty()) {
               for (var record : records) {
                   //LocalDateTime now = LocalDateTime.now();
                   String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms").format(record.timestamp());
                   System.out.println("["+ data +"]["+ record.topic() + "][" + record.key()+"][" + record.value() + "][Partition:" + record.partition() + "][Offset: " + record.offset() +"]");  // chave
                   try {
                       Thread.sleep(000);
                   } catch (InterruptedException e) {
                       // ignorando
                       e.printStackTrace();
                   }
               }
           }


       }


    }
    private static Properties properties(){
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.01:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // transforma bytes em string
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // transforma bytes em string
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());

        return properties;

    }
}
