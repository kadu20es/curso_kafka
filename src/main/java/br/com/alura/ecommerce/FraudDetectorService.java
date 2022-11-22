package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class FraudDetectorService {

    public static void main(String[] args) {

        var consumer = new KafkaConsumer<String, String>(properties());
        consumer.subscribe(Collections.singletonList("ECOMMERCE_NEW_ORDER")); // Se inscreve em um tópico
       while(true) {
           var records = consumer.poll(Duration.ofMillis(100));
           if (!records.isEmpty()) {
               System.out.println("Encontrei " + records.count() + " registros");
               for (var record : records) {
                   System.out.println("---------------------------------------------------------");
                   System.out.println(Instant.now());
                   System.out.println("Processando new order. Checking for fraud");
                   System.out.println(record.key());  // chave
                   System.out.println(record.value()); // valor da mensagem
                   System.out.println(record.partition()); // partição onde foi enviada
                   System.out.println(record.offset()); // offset da mensagem
                   try {
                       Thread.sleep(0);
                   } catch (InterruptedException e) {
                       // ignorando
                       e.printStackTrace();
                   }
                   System.out.println("Order processed");
               }
           }


       }


    }
    private static Properties properties(){
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.01:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // transforma bytes em string
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // transforma bytes em string
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, FraudDetectorService.class.getSimpleName() + "-" + UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");  // comita de um em um
        return properties;

    }
}
