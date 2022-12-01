package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {
    final String orderId;
    final String userId;
    final BigDecimal amount;


    public Order(String orderId, String userId, BigDecimal amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
    }
}
