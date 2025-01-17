package com.vinio.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE_NAME = "defaultExchange";

    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String queueName, String message) {
        String routingKey = getRoutingKeyForQueue(queueName);
        if (routingKey == null) {
            System.out.println("Invalid queue name");
        } else {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, message);
        }
    }

    private String getRoutingKeyForQueue(String queueName) {
        return switch (queueName) {
            case "MainQueue" -> "main.key";
            default -> null;
        };
    }
}
