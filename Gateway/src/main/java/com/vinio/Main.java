package com.vinio;

import org.vinio.product.grpc.Product;

public class Main {
    public static void main(String[] args) {
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
