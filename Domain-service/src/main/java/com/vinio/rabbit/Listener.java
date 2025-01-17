package com.vinio.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Listener {
    static final String queueName = "QueueToDomain";

    @Autowired
    private Sender sender;

    @Bean
    public Queue myQueue() {
        return new Queue(queueName, false);
    }

    @RabbitListener(queues = queueName)
    public void listen(String message) {
        System.out.println(message);
    }
}
