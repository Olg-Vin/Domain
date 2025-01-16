package com.vinio;

import com.vinio.rabbit.ProductService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GrpcServer {

    private final int port = 50051;
    private final Server server;

    public GrpcServer() {
        this.server = ServerBuilder.forPort(port)
                .addService(new ProductService())
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("gRPC Server started at port " + port);
    }

    public static void main(String[] args) throws IOException {
        final GrpcServer grpcServer = new GrpcServer();
        grpcServer.start();
    }
}
