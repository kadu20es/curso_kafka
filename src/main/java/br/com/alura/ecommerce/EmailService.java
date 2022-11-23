package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.text.SimpleDateFormat;

public class EmailService {

    public static void main(String[] args) {
        var emailService = new EmailService();
        try (var service = new KafkaService(EmailService.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL",
                 emailService::parse)) { // method reference - passando uma referência para a função (quero que você invoce essa função para cada record)
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms").format(record.timestamp());
        System.out.println("[" + data + "][Sending mail " + record.key() + "][Value " + record.value() + "][Record partition:  " + record.partition() + "][Offset:  " + record.offset() + "]");
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            // ignorando
            e.printStackTrace();
        }
        System.out.println("Email sent");
    }

}
