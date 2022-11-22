package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;

public class LogService2 {

    public static void main(String[] args) {

        var consumer = new KafkaConsumer<String, String>(properties());
        consumer.subscribe(Collections.singletonList("ECOMMERCE_SEND_EMAIL")); // Se inscreve em um tópico
       while(true) {
           var records = consumer.poll(Duration.ofMillis(100));
           if (!records.isEmpty()) {
               System.out.println("Encontrei " + records.count() + " registros");
               for (var record : records) {
                   System.out.println("---------------------------------------------------------");
                   System.out.println(Instant.now());
                   System.out.println("Sending email");
                   System.out.println(record.key());  // chave
                   System.out.println(record.value()); // valor da mensagem
                   System.out.println(record.partition()); // partição onde foi enviada
                   System.out.println(record.offset()); // offset da mensagem
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       // ignorando
                       e.printStackTrace();
                   }
                   System.out.println("Email sent");
               }
           }


       }


    }
    private static Properties properties(){
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.01:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // transforma bytes em string
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // transforma bytes em string
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService2.class.getSimpleName());

        return properties;

    }
}
