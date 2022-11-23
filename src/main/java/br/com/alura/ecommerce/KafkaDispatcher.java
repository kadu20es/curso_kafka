package br.com.alura.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

class KafkaDispatcher implements Closeable {

    private final KafkaProducer<String, String> producer;

    KafkaDispatcher() {
        producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {

        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092"); // fala onde conectar ao kafka
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // transformar string em bytes, serializando as chaves
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName()); // serializa strings das mensagens em bytes
        return properties;
    }

    void send(String topic, String key, String value) throws ExecutionException, InterruptedException {
        var record = new ProducerRecord<>(topic, key, value);
        Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms").format(data.timestamp());
            System.out.println("[" + date + "][Success! Sending message \"" + data.topic() + "\"][partition " + data.partition() + "][Offset " + data.offset() + "]");
        };
        producer.send(record, callback).get(); // envia o registro para o kafka
    }

    @Override
    public void close() {
        producer.close();
    }
}
