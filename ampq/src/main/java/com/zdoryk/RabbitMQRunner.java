package com.zdoryk;

import com.zdoryk.config.SendMessageVerificationRabbitQueueConfig;
import com.zdoryk.util.EmailVerificationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQRunner {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQRunner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

    }

    public void sendEmailVerification(EmailVerificationToken message){
        log.info("sending message {}", message);
        System.out.println("Sending Message...");
        rabbitTemplate.convertAndSend(
                SendMessageVerificationRabbitQueueConfig.topicExchangeName,
                "internal.confirm",
                message
        );
    }

//    public void updateOrder(Send orderUpdateRabbitRequest){
//        log.info("sending message {}", orderUpdateRabbitRequest);
//        rabbitTemplate.convertAndSend(OrderUpdateRabbitQueueConfig.topicExchangeName,"internal.update", orderUpdateRabbitRequest);
//    }

}