package com.vinio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vinio.product.grpc.Product;

@SpringBootApplication
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String host = "localhost";
        int port = 50051;

        try (ProductClient client = new ProductClient(host, port)) {
            String productId = "1";

            Product product = client.getProduct(productId);

            if (product != null) {
                System.out.println("Получен продукт: " + product);
            } else {
                System.out.println("Продукт не найден.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
