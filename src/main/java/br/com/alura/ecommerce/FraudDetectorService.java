package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.text.SimpleDateFormat;

public class FraudDetectorService {

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        try (var service = new KafkaService(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                fraudService::parse)) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms").format(record.timestamp());
        System.out.println("[" + data + "][Processing new order. Checking for fraud " + record.key() + "][Value " + record.value() + "][Record partition:  " + record.partition() + "][Offset:  " + record.offset() + "]");
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            // ignorando
            e.printStackTrace();
        }
        System.out.println("Order processed");
    }
}
