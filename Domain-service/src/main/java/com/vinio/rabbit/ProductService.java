package com.vinio.rabbit;

import io.grpc.stub.StreamObserver;
import org.vinio.product.grpc.*;

public class ProductService extends ProductServiceGrpc.ProductServiceImplBase {
    private final com.vinio.database.ProductService productService;
    public ProductService(com.vinio.database.ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void createProduct(Product request, StreamObserver<ProductResponse> responseObserver) {

    }

    @Override
    public void getProduct(ProductRequest request, StreamObserver<Product> responseObserver) {
        super.getProduct(request, responseObserver);
    }

    @Override
    public void getAllProducts(Empty request, StreamObserver<ProductList> responseObserver) {
        super.getAllProducts(request, responseObserver);
    }

    @Override
    public void updateProduct(Product request, StreamObserver<ProductResponse> responseObserver) {
        super.updateProduct(request, responseObserver);
    }

    @Override
    public void deleteProduct(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        super.deleteProduct(request, responseObserver);
    }
}