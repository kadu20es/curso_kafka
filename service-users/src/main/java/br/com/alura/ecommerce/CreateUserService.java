package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class CreateUserService {

    private final Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        this.connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("create table USERS (" +
                    "uuid varchar(200) primary key," +
                    "email varchar(100))");
        } catch (SQLException ex) {
            // be careful, the sql could be wrong
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        var createUserService = new CreateUserService();
        try (var service = new KafkaService<>(CreateUserService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER",
                createUserService::parse,
                Order.class,
                new HashMap<>())){
            service.run();
        }
    }

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:ms").format(record.timestamp());
        System.out.println("[" + data + "][Processing new order. Checking for new user.][Value: " + record.value() + "]");

        var order = record.value();
        if (isNewUser(order.getEmail())) {
            insertNewUser(order.getUserId(), order.getEmail());
        }
    }

    private void insertNewUser(String uuid, String email) throws SQLException {
        var insert = connection.prepareStatement("insert into USERS (uuid, email) values(?,?) ");
        insert.setString(1, uuid);
        insert.setString(2, email);
        insert.execute();
        System.out.println("Usuário " + uuid + " e " + email + " adicionado.");
    }

    private boolean isNewUser(String email) throws SQLException {
        // verifica se o usuário é novo
        var exists = connection.prepareStatement("select uuid from USERS " +
                "where email = ?");
        exists.setString(1, email);
        var result = exists.executeQuery();
        return !result.next(); // se existir um usuário, vai para a próxima linha
    }


}
