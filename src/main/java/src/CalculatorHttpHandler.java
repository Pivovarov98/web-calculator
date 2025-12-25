package src;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class CalculatorHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String query = exchange.getRequestURI().getQuery();

        try (OutputStream os = exchange.getResponseBody()) {

            if (query == null) {
                exchange.sendResponseHeaders(400, 0);
                os.write("There is no parameters. Try something like \"http://localhost:8080/calc?num1=10&num2=20&arg=+\"".getBytes());
            }

            String[] args = query.split("&");

            String resp = "Unknown argument";
            boolean isArgValid = true;
            double num1 = Double.parseDouble(args[0].split("=")[1]);
            double num2 = Double.parseDouble(args[1].split("=")[1]);
            double sum = 0;

            exchange.sendResponseHeaders(200, 0);

            switch (args[2].split("=")[1]) {
                case "+" -> sum = num1 + num2;
                case "-" -> sum = num1 - num2;
                case "*" -> sum = num1 * num2;
                case "/" -> sum = num1 / num2;
                default -> isArgValid = false;
            }

            if (!isArgValid) {
                os.write(resp.getBytes());
                return;
            }

            resp = "Result =  " + sum;

            os.write(resp.getBytes());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
