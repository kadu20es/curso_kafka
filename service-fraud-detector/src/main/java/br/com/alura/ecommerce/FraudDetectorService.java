package br.com.alura.ecommerce;


import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        try (var service = new KafkaService<>(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                fraudService::parse,
                Order.class,
                new HashMap<>())){
            service.run();
        }
    }

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms").format(record.timestamp());
        System.out.println("[" + data + "][Processing new order. Checking for fraud.][Value: " + record.value() + "][Record partition:  " + record.partition() + "][Offset:  " + record.offset() + "]");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignorando
            e.printStackTrace();
        }

        var order = record.value();
        if (isaFraud(order)){
            /* figindo que a fraude acontece quando o valor for >= 4500
             se aprovado, loga e envia para um tópico e esse tópico será lido pelo service-log */
            System.out.println("Order is a fraud!!!!! " + order);
            orderDispatcher.send(
                    "ECOMMERCE_ORDER_REJECTED",
                    order.getUserId(),
                    order);
        } else {
            System.out.println("Order approved! " + order);
            orderDispatcher.send(
                    "ECOMMERCE_ORDER_APPROVED",
                    order.getUserId(),
                    order);
        }
        
        System.out.println("Order processed");
    }

    private static boolean isaFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }
}
