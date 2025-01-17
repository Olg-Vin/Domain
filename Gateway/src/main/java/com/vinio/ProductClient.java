package com.vinio;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.vinio.rabbit.Sender;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vinio.product.grpc.*;

import java.util.concurrent.TimeUnit;

@Component
public class ProductClient implements AutoCloseable {
    private ManagedChannel channel;
    private ProductServiceGrpc.ProductServiceBlockingStub blockingStub;
    @Autowired
    Sender sender;

    ProductClient() {

    }

    public ProductClient(String host, int port) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = ProductServiceGrpc.newBlockingStub(channel);
    }

    public Product getProduct(String productId) {
        // Создаём запрос для получения продукта с указанным id
        ProductGetRequest productRequest = ProductGetRequest.newBuilder()
                .setId(Integer.parseInt(productId))  // Устанавливаем id продукта
                .build();

        System.out.println("\n3. Получите productRequest из билдера\n" + productRequest);

        try {
            return blockingStub.getProduct(productRequest);
        } catch (StatusRuntimeException e) {
            System.err.println("Failed: " + e.getStatus());
        }

        return null;
    }

    public ProductList getAllProducts() {
        try {
            return blockingStub.getAllProducts(Empty.newBuilder().build());
        } catch (StatusRuntimeException e) {
            System.err.println("Failed: " + e.getStatus());
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted while shutting down channel");
        }
    }
}