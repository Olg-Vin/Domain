package com.vinio.rabbit;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.vinio.product.grpc.SimpleResponse;

@Service
public class Listener {

    public Listener() {
    }

    @RabbitListener(queues = "QueueToGateway")
    public void listen(byte[] message) throws InvalidProtocolBufferException {
        SimpleResponse productOtherRequest = SimpleResponse.parseFrom(message);
        System.out.println(productOtherRequest);
    }
}