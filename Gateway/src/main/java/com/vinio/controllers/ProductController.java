package com.vinio.controllers;

import com.vinio.ProductClient;
import com.vinio.rabbit.Sender;
import io.grpc.StatusRuntimeException;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.product.grpc.Product;
import org.vinio.product.grpc.ProductOtherRequest;

import javax.naming.ServiceUnavailableException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final Sender sender;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Value("${grpc.host}")
    private String host;

    @Value("${grpc.port}")
    private int port;

    public ProductController(Sender sender) {
        this.sender = sender;
    }

    // GET запрос: Получить продукт по ID через gRPC
    @GetMapping("/{id}")
    @Cacheable(value = "productCache", key = "#id", unless = "#result == null")
    @Timed(value="products.get", description = "Time to process the get request for a single product")
    public ProductResponse getProduct(@PathVariable String id) throws Exception {
        logger.info("Incoming GET request for product with id: {}", id);  // Логирование входящего запроса
        try (ProductClient client = new ProductClient(host, port)) {
            logger.info("Sending gRPC request to get product by id: {}", id);  // Логирование исходящего запроса
            Product product = client.getProduct(id);

//            if (product == null) {
//                throw new RuntimeException("Product not found");
//            }

            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setName(product.getName());
            response.setPrice(product.getPrice());
            response.setCategory(product.getCategory());
            response.setCount(product.getCount());

            logger.info("Received product response: {}", response);  // Логирование ответа

            return response;
        } catch (NullPointerException e) {
            logger.error("Error fetching products", e);
            throw new NullPointerException();
        }
    }

    // GET запрос: Получить все продукты через gRPC
    @GetMapping
    @Cacheable(value = "productCache", key = "'all'", unless = "#result == null")
    @Timed(value="products.getAll", description = "Time to process the get request for all products")
    public List<ProductResponse> getAllProducts() {
        logger.info("Incoming GET request to fetch all products");
        try (ProductClient client = new ProductClient(host, port)) {
            logger.info("Sending gRPC request to get all products");
            var productList = client.getAllProducts();

            if (productList == null || productList.getProductsList().isEmpty()) {
                logger.info("No products found");
                return List.of();
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

            logger.info("Received product list response with {} products", responses.size());
            return responses;
        } catch (Exception e) {
            logger.error("Error fetching products", e);
            throw new RuntimeException("Internal server error");
        }
    }

    // POST запрос: Создать новый продукт через RabbitMQ
    @PostMapping
    @CacheEvict(value = "productCache", allEntries = true)
    @Timed(value="products.create", description = "Time to process the create product request")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest request) {
        logger.info("Incoming POST request to create product: {}", request);
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

            logger.info("Sending product creation message via RabbitMQ: {}", message);
            sender.sendMessage(message.toByteArray());
            return ResponseEntity.ok("Product creation message sent via RabbitMQ");
        } catch (Exception e) {
            logger.error("Error fetching products", e);
            throw new RuntimeException("Internal server error" + e);
        }
    }

    // PUT запрос: Обновить продукт через RabbitMQ
    @PutMapping("/{id}")
    @CacheEvict(value = "productCache", allEntries = true)
    @Timed(value="products.update", description = "Time to process the update product request")
    public ResponseEntity<String> updateProduct(@PathVariable String id, @RequestBody ProductRequest request) {
        logger.info("Incoming PUT request to update product with id: {}", id);
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

            logger.info("Sending product update message via RabbitMQ: {}", message);
            sender.sendMessage(message.toByteArray());
            return ResponseEntity.ok("Product update message sent via RabbitMQ");
        } catch (Exception e) {
            logger.error("Error fetching products", e);
            throw new RuntimeException("Internal server error" + e);
        }
    }

    // DELETE запрос: Удалить продукт через RabbitMQ
    @DeleteMapping("/{id}")
    @CacheEvict(value = "productCache", allEntries = true)
    @Timed(value="products.delete", description = "Time to process the delete product request")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        logger.info("Incoming DELETE request to delete product with id: {}", id);
        try {
            Product product = Product.newBuilder()
                    .setId(Integer.parseInt(id))
                    .build();

            ProductOtherRequest message = ProductOtherRequest.newBuilder()
                    .setType("delete")
                    .setProduct(product)
                    .build();

            logger.info("Sending product deletion message via RabbitMQ: {}", message);
            sender.sendMessage(message.toByteArray());
            return ResponseEntity.ok("Product deletion message sent via RabbitMQ");
        } catch (Exception e) {
            logger.error("Error fetching products", e);
            throw new RuntimeException("Internal server error" + e);
        }
    }
}
