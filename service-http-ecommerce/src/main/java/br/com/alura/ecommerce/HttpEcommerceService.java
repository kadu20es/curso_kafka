package br.com.alura.ecommerce;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Cria um servidor HTTP para as requisições de novas compras
 */
public class HttpEcommerceService {

    public static void main(String[] args) throws Exception {
        var server = new Server(8080); // cria um servidor
        var context = new ServletContextHandler(); // quando alguém chamar uma requisição, lida com ela através de um contexto
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new NewOrderServlet()), "/new"); // quando alguém chamar localhost:8080/ adiciona uma servlet que faz uma new order

        server.setHandler(context);

        server.start();
        server.join(); // espera o servidor terminar para terminar a aplicação

    }


}
