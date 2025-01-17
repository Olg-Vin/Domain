package com.vinio.rabbit;

import com.vinio.database.ProductEntity;
import com.vinio.database.ProductServiceImpl;
import io.grpc.stub.StreamObserver;
//import org.lognet.springboot.grpc.GRpcService;
import org.springframework.stereotype.Component;
import org.vinio.product.grpc.*;

//@GRpcService
@Component
public class ProductService extends ProductServiceGrpc.ProductServiceImplBase {
    private final ProductServiceImpl productServiceImpl;

    public ProductService(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @Override
    public void createProduct(Product request, StreamObserver<ProductResponse> responseObserver) {

    }

    @Override
    public void getProduct(ProductRequest request, StreamObserver<Product> responseObserver) {
        System.out.println("Запрос на получение данных:\n" + request);
        Product.Builder product = Product.newBuilder();

        ProductEntity productEntity = productServiceImpl.getProductById(request.getId())
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));
        System.out.println("Продукт найден: " + productEntity);

        product.setId(productEntity.getId())
                .setName(productEntity.getName())
                .setPrice(productEntity.getPrise())
                .setCategory(productEntity.getCategory())
                .setCount(productEntity.getCount());

//        product.setId(1)
//                .setName("name")
//                .setPrice(10)
//                .setCategory("category")
//                .setCount(5);

        responseObserver.onNext(product.build());
        responseObserver.onCompleted();
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