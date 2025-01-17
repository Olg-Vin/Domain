package com.vinio;

import com.vinio.database.ProductServiceImpl;
import com.vinio.rabbit.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

@Component
public class GrpcServer {

    private final int port = 50051;
    private final Server server;
    private final ProductServiceImpl productServiceImpl;

    @Autowired
    public GrpcServer(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
        this.server = ServerBuilder.forPort(port)
                .addService(new ProductService(productServiceImpl))  // Используем инжектированный productService
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("gRPC Server started at port " + port);
    }
}
