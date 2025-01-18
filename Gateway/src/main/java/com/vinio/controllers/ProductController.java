package com.vinio.controllers;

import com.vinio.ProductClient;
import com.vinio.rabbit.Sender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.product.grpc.Product;
import org.vinio.product.grpc.ProductOtherRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Sender sender;

    public ProductController(Sender sender) {
        this.sender = sender;
    }

    private final String host = "localhost";
    private final int port = 50051;

    // GET запрос: Получить продукт по ID через gRPC
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
        try (ProductClient client = new ProductClient(host, port)) {
            Product product = client.getProduct(id);

            if (product == null) {
                return ResponseEntity.notFound().build();
            }

            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setName(product.getName());
            response.setPrice(product.getPrice());
            response.setCategory(product.getCategory());
            response.setCount(product.getCount());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET запрос: Получить все продукты через gRPC
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        try (ProductClient client = new ProductClient(host, port)) {
            var productList = client.getAllProducts();

            if (productList == null || productList.getProductsList().isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<ProductResponse> responses = productList.getProductsList().stream().map(product -> {
                ProductResponse response = new ProductResponse();
                response.setId(product.getId());
                response.setName(product.getName());
                response.setPrice(product.getPrice());
                response.setCategory(product.getCategory());
                response.setCount(product.getCount());
                return response;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // POST запрос: Создать новый продукт через RabbitMQ
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest request) {
        try {
            Product product = Product.newBuilder()
                    .setName(request.getName())
                    .setPrice(request.getPrice())
                    .setCategory(request.getCategory())
                    .setCount(request.getCount())
                    .build();

            ProductOtherRequest message = ProductOtherRequest.newBuilder()
                    .setType("post")
                    .setProduct(product)
                    .build();

            sender.sendMessage(message.toByteArray());
            return ResponseEntity.ok("Product creation message sent via RabbitMQ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // PUT запрос: Обновить продукт через RabbitMQ
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable String id, @RequestBody ProductRequest request) {
        try {
            Product product = Product.newBuilder()
                    .setId(Integer.parseInt(id))
                    .setName(request.getName())
                    .setPrice(request.getPrice())
                    .setCategory(request.getCategory())
                    .setCount(request.getCount())
                    .build();

            ProductOtherRequest message = ProductOtherRequest.newBuilder()
                    .setType("put")
                    .setProduct(product)
                    .build();

            sender.sendMessage(message.toByteArray());
            return ResponseEntity.ok("Product update message sent via RabbitMQ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // DELETE запрос: Удалить продукт через RabbitMQ
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        try {
            Product product = Product.newBuilder()
                    .setId(Integer.parseInt(id))
                    .build();

            ProductOtherRequest message = ProductOtherRequest.newBuilder()
                    .setType("delete")
                    .setProduct(product)
                    .build();

            sender.sendMessage(message.toByteArray());
            return ResponseEntity.ok("Product deletion message sent via RabbitMQ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
