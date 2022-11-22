package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.text.SimpleDateFormat;

public class EmailService {

    public static void main(String[] args) {
        var emailService = new EmailService();
        var service = new KafkaService(EmailService.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL",
                 emailService::parse); // method reference - passando uma referência para a função (quero que você invoce essa função para cada record)
        service.run();
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("---------------------------------------------------------");
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms").format(record.timestamp());
        System.out.println(data);
        System.out.println("Sending email");
        System.out.println(record.key());  // chave
        System.out.println(record.value()); // valor da mensagem
        System.out.println(record.partition()); // partição onde foi enviada
        System.out.println(record.offset()); // offset da mensagem
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            // ignorando
            e.printStackTrace();
        }
        System.out.println("Email sent");
    }

}
