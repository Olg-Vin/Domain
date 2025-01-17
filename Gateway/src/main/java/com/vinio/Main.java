package com.vinio;

import com.vinio.rabbit.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vinio.product.grpc.Product;
import org.vinio.product.grpc.ProductList;
import org.vinio.product.grpc.ProductOtherRequest;

@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    Sender sender;
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
            ProductList productList = client.getAllProducts();

            if (product != null) {
                System.out.println("Получен продукт: " + product);
                System.out.println("Получен список продуктов: " + productList);
            } else {
                System.out.println("Продукт не найден.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sender.sendMessage("Hi from gateway");

        ProductOtherRequest.Builder productOtherBuilder = ProductOtherRequest.newBuilder();
        Product product = Product.newBuilder()
                .setId(11)
                .setName("name")
                .setCount(1000)
                .setCategory("new category")
                .setPrice(555.555)
                .build();
        productOtherBuilder.setType("post")
                        .setProduct(product);
        sender.sendMessage(productOtherBuilder.build());
    }
}
