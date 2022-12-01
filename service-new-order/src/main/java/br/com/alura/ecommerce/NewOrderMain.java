package br.com.alura.ecommerce;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<String>()) {

                for (var i = 0; i < 30; i++) {

                    var userId = UUID.randomUUID().toString();
                    var orderId = Integer.toString(ThreadLocalRandom.current().nextInt(1, 10000000+1));
                    var amount = BigDecimal.valueOf(Math.random() * 5000 + 1).setScale(2, RoundingMode.FLOOR);

                    var order = new Order(orderId, userId, amount);
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER",userId, order);

                    var email = "Thank you for your order! We are processing your order!";
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);
                }
            }
        }
    }
}
