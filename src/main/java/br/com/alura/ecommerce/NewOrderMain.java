package br.com.alura.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Instant;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(properties());

        for (var i = 0; i < 100; i++) {


            var key = UUID.randomUUID().toString();
            var value = key + "-67523,5364";
            var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", key, value);
            Callback callback = (data, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                System.out.println("[" + Instant.now() + "]" + " sucesso enviando " + data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp " + data.timestamp());
            };
            var email = "Thank yout for your order!";
            var orderN = "Order nº 45457";
            var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", email, orderN);
            //var fraudDetector = new ProducerRecord<>("ECOMMERCE_FRAUD_DETECTOR", orderN, orderN);
            producer.send(record, callback).get(); // envia o registro para o kafka
            //----------------------------------------
            producer.send(emailRecord, callback).get();
            //producer.send(fraudDetector, callback).get();
        }
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092"); // fala onde conectar ao kafka
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // transformar string em bytes, serializando as chaves
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // serializa strings das mensagens em bytes
        return properties;
    }

}
