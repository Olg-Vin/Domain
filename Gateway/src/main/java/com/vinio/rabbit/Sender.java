package com.vinio.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.vinio.product.grpc.ProductOtherRequest;

@Component
public class Sender {
    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE_NAME = "productExchange";
    private static final String ROUTING_KEY = "to.domain.key";
    private static final String QUEUE_NAME = "queueToDomain";


    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }

    public void sendMessage(ProductOtherRequest message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }
}