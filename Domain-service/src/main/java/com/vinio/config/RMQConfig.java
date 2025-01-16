package com.vinio.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RMQConfig {

    static final String EXCHANGE_NAME = "defaultExchange";
    static final String QUEUE_NAME = "GRPCQueue";
    static final String ROUTING_KEY = "grpcRoutingKey";

    @Bean
    public Exchange exchange() {
        return new DirectExchange(EXCHANGE_NAME, false, false);
    }

    // Создание очереди с указанным именем
    @Bean
    public Queue grpcQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    // Создание привязок для очереди
    @Bean
    public Binding bindingGrpcQueue(Queue grpcQueue, Exchange exchange) {
        return BindingBuilder.bind(grpcQueue).to(exchange).with(ROUTING_KEY).noargs();
    }
}
