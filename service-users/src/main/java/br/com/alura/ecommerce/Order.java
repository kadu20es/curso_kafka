package br.com.alura.ecommerce;

import java.math.BigDecimal;

public class Order {
    private final String orderId, userId, email;
    private final BigDecimal amount;


    public Order(String orderId, String userId, BigDecimal amount, String email) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.email = email;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId :'" + orderId + '\'' +
                ", userId :'" + userId + '\'' +
                ", amount : R$ " + amount + '\'' +
                ", email :" + email +
                '}';
    }
}
