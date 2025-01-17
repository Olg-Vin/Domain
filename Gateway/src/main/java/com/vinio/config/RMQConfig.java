package com.vinio.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для двух очередей
 * QueueToDomain - очередь, которую слушает domain-сервис
 * QueueToGateway - очередь, которую слушает gateway-сервис
 */
@Configuration
public class RMQConfig {
    static final String EXCHANGE_NAME = "productExchange";

    public enum QueueNames {
        QUEUE_TO_DOMAIN("QueueToDomain"),
        QUEUE_TO_GATEWAY("QueueToGateway");

        private final String name;

        QueueNames(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Bean
    public Exchange exchange() {
        return new DirectExchange(EXCHANGE_NAME, false, false);
    }

    // Создание очереди для domain
    @Bean(name = "queueToDomain")
    public Queue queueToDomain() {
        return createQueue(QueueNames.QUEUE_TO_DOMAIN);
    }

    // Создание очереди для gateway
    @Bean(name = "queueToGateway")
    public Queue queueToGateway() {
        return createQueue(QueueNames.QUEUE_TO_GATEWAY);
    }

    private Queue createQueue(QueueNames queueName) {
        return new Queue(queueName.getName(), false);
    }

    // Создание привязки для QueueToDomain
    @Bean
    public Binding bindingQueueToDomain(@Qualifier("queueToDomain") Queue queueToDomain, Exchange exchange) {
        return createBinding(queueToDomain, exchange, "to.domain.key");
    }

    // Создание привязки для QueueToGateway
    @Bean
    public Binding bindingQueueToGateway(@Qualifier("queueToGateway") Queue queueToGateway, Exchange exchange) {
        return createBinding(queueToGateway, exchange, "to.gateway.key");
    }

    private Binding createBinding(Queue queue, Exchange exchange, String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }
}
