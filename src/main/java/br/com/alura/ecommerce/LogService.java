package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) {

        var logService = new LogService();
        try (var service = new KafkaService(LogService.class.getSimpleName(),
                Pattern.compile("ECOMMERCE.*"),
                logService::parse)){
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms").format(record.timestamp());
        System.out.println("["+ data +"]["+ record.topic() + "][" + record.value() + "][Partition:" + record.partition() + "][Offset: " + record.offset() +"]");  // chave
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            // ignorando
            e.printStackTrace();
        }
    }
}
