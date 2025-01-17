package com.vinio;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.vinio.product.grpc.*;

import java.util.concurrent.TimeUnit;

public class ProductClient implements AutoCloseable {
    private final ManagedChannel channel;
    private final ProductServiceGrpc.ProductServiceBlockingStub blockingStub;

    public ProductClient(String host, int port) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = ProductServiceGrpc.newBlockingStub(channel);
    }

    public Product getProduct(String productId) {
        // Создаём запрос для получения продукта с указанным id
        ProductRequest productRequest = ProductRequest.newBuilder()
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