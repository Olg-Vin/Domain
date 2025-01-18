package org.vinio.rabbit;

import com.google.protobuf.InvalidProtocolBufferException;
import org.vinio.database.ProductEntity;
import org.vinio.database.ProductServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vinio.product.grpc.Product;
import org.vinio.product.grpc.ProductOtherRequest;
import org.vinio.product.grpc.SimpleResponse;

@Service
public class Listener {

    private final ProductServiceImpl productService;
    private final Sender sender;

    @Autowired
    public Listener(ProductServiceImpl productService, Sender sender) {
        this.productService = productService;
        this.sender = sender;
    }

    @RabbitListener(queues = "QueueToDomain") // Укажите вашу очередь
    public void listen(byte[] message) throws InvalidProtocolBufferException {
        // Десериализация входящего сообщения
        ProductOtherRequest productOtherRequest = ProductOtherRequest.parseFrom(message);
        String type = productOtherRequest.getType();
        Product product = productOtherRequest.getProduct();
        SimpleResponse.Builder response = SimpleResponse.newBuilder();

        try {
            switch (type.toLowerCase()) {
                case "post":
                    // Создаем новый продукт
                    ProductEntity productEntity = new ProductEntity();
                    productEntity.setName(product.getName());
                    productEntity.setPrise(product.getPrice());
                    productEntity.setCategory(product.getCategory());
                    productEntity.setCount(product.getCount());
                    ProductEntity productEntityResponse = productService.createProduct(productEntity);
                    response.setStatus("Product created successfully. ID = " + productEntityResponse.getId());
                    break;

                case "put":
                    // Обновляем продукт
                    if (product.getId() == 0) {
                        response.setStatus("ID is required for update.");
                    } else {
                        ProductEntity updatedProductEntity = new ProductEntity();
                        updatedProductEntity.setId(product.getId());
                        updatedProductEntity.setName(product.getName());
                        updatedProductEntity.setPrise(product.getPrice());
                        updatedProductEntity.setCategory(product.getCategory());
                        updatedProductEntity.setCount(product.getCount());
                        productService.updateProduct(product.getId(), updatedProductEntity);
                        response.setStatus("Product updated successfully.");
                    }
                    break;

                case "delete":
                    // Удаляем продукт
                    if (product.getId() == 0) {
                        response.setStatus("ID is required for deletion.");
                    } else {
                        productService.deleteProduct(product.getId());
                        response.setStatus("Product deleted successfully.");
                    }
                    break;

                default:
                    response.setStatus("Invalid message type.");
            }
        } catch (Exception e) {
            response.setStatus("Error: " + e.getMessage());
        }

        byte[] serializedMessage = response.build().toByteArray();
        sender.sendMessage(serializedMessage);
    }
}
