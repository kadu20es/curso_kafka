package br.com.alura.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

public class GsonSerializer<T> implements Serializer<T> {

    private final Gson gson = new GsonBuilder().create();

    @Override
    public byte[] serialize(String topic, T data) {
        return gson.toJson(data).getBytes();
    }
}

// transforma Order em Json e em seguida em bytes
// serializa através do GsonBuilder().create(), contudo, para o Kafka precisa implementar o serialize, então serializa o String para T e em seguida transforma em bytes