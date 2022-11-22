package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.text.SimpleDateFormat;

public class FraudDetectorService {

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        var service = new KafkaService(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                fraudService::parse);
        service.run();
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("---------------------------------------------------------");
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms").format(record.timestamp());
        System.out.println(data);
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
