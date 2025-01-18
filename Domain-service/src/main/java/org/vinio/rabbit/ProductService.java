package org.vinio.rabbit;

import org.vinio.database.ProductEntity;
import org.vinio.database.ProductServiceImpl;
import io.grpc.stub.StreamObserver;
//import org.lognet.springboot.grpc.GRpcService;
import org.springframework.stereotype.Component;
import org.vinio.product.grpc.*;

import java.util.List;

//@GRpcService
@Component
public class ProductService extends ProductServiceGrpc.ProductServiceImplBase {
    private final ProductServiceImpl productServiceImpl;

    public ProductService(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    @Override
    public void getProduct(ProductGetRequest request, StreamObserver<Product> responseObserver) {
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

        responseObserver.onNext(product.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllProducts(Empty request, StreamObserver<ProductList> responseObserver) {
        System.out.println("Запрос на получение данных:");
        ProductList.Builder productListBuilder = ProductList.newBuilder();

        List<ProductEntity> productEntityList = productServiceImpl.getAllProducts();
        System.out.println("Продукты найдены: " + productEntityList);

        for (ProductEntity entity : productEntityList) {
            Product product = Product.newBuilder()
                    .setId(entity.getId())
                    .setName(entity.getName())
                    .setPrice(entity.getPrise())
                    .setCategory(entity.getCategory())
                    .setCount(entity.getCount())
                    .build();
            productListBuilder.addProducts(product);
        }

        responseObserver.onNext(productListBuilder.build());
        responseObserver.onCompleted();
    }
}