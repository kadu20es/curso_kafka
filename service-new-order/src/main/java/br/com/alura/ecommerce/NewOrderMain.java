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

                for (var i = 0; i < 10; i++) {

                    //var userId = UUID.randomUUID().toString();
                    var orderId = Integer.toString(ThreadLocalRandom.current().nextInt(1, 10000000+1));
                    var amount = BigDecimal.valueOf(Math.random() * 5000 + 1).setScale(2, RoundingMode.FLOOR);
                    var email = Math.random() + "@email.com";

                    var order = new Order(orderId, email, amount);
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);

                    var message = "Thank you for your order! We are processing your order!";
                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, message);
                }
            }
        }
    }
}
