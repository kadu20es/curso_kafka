package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class FraudDetectorService {

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        try (var service = new KafkaService<>(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                fraudService::parse,
                Order.class,
                new HashMap<>())){
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) {
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms").format(record.timestamp());
        System.out.println("[" + data + "][Processing new order. Checking for fraud.][Value: " + record.value() + "][Record partition:  " + record.partition() + "][Offset:  " + record.offset() + "]");
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            // ignorando
            e.printStackTrace();
        }
        System.out.println("Order processed");
    }
}
