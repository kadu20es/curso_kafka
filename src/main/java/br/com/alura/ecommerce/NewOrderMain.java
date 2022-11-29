package br.com.alura.ecommerce;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var mailDispatcher = new KafkaDispatcher<String>()) {

                for (var i = 0; i < 10; i++) {

                    var userId = UUID.randomUUID().toString();
                    //var orderId = Double.toString(Math.random() * (1000000 - 2) + 2);
                    var orderId = Integer.toString(ThreadLocalRandom.current().nextInt(1, 10000000+1));
                    var amount = BigDecimal.valueOf(Math.random() * 5000 + 1).setScale(2, RoundingMode.FLOOR);
                    var order = new Order(orderId, userId, amount);

                    orderDispatcher.send("ECOMMERCE_NEW_ORDER",userId, order);

                    var email = "Thank you for your order! We are processing your order!";
                    mailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);
                }
            }
        }
    }

    /*
    movido para KafkaDispatcher
    private static Properties properties() {

        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092"); // fala onde conectar ao kafka
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // transformar string em bytes, serializando as chaves
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // serializa strings das mensagens em bytes
        return properties;
    }
    */


}
