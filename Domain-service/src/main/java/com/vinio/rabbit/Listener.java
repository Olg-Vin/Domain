package com.vinio.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Listener {
    static final String queueName = "GRPCQueue";

    @Autowired
    private Sender sender;

    @Bean
    public Queue myQueue() {
        return new Queue(queueName, false);
    }

    @RabbitListener(queues = queueName)
    public void listen(String message) {
        /*System.out.println("1. Message read from " + queueName + " : " + message);
//        try (QueueClient client = new QueueClient("grpc-backend-service", 8081)) {
        try (QueueClient client = new QueueClient("localhost", 8081)) {
            UpdateMessage updateMessage = client.getAnalyze(message);
            System.out.println("\n2. Update message:\n" + updateMessage);
            sender.sendMessage("MainQueue", JsonFormat.printer().print(updateMessage));
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }*/
    }
}