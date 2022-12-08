package br.com.alura.ecommerce;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class NewOrderServlet extends HttpServlet {

    // início da injeção de dependência

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    // fim da injeção de dependência

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            var email = req.getParameter("email"); // obtém os dados da requisição (navegador http://localhost:8080/new?email=kadu20es@gmail.com&amount=499.90"
            var amount = new BigDecimal(req.getParameter("amount"));

            var orderId = Integer.toString(ThreadLocalRandom.current().nextInt(1, 10000000 + 1));
            var order = new Order(orderId, email, amount);
            orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);

            var message = "Thank you for your order! We are processing your order!";
            emailDispatcher.send("ECOMMERCE_SEND_EMAIL", email, message);

            System.out.println("New order sent successfully."); // imprime no terminal
            resp.setStatus(HttpServletResponse.SC_OK); // envia um "200" como resposta
            resp.getWriter().println("New order sent successfully."); // imprime na tela do usuário


        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }
    }

}
