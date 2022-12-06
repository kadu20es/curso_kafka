package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {
    private final String orderId, userId, email;
    private final BigDecimal amount;


    public Order(String orderId, String userId, String email, BigDecimal amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.email = email;
        this.amount = amount;
    }
}
