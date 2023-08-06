package com.zdoryk.rabbitmq;

import com.zdoryk.email.EmailService;
import com.zdoryk.util.EmailVerificationToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailNotificationConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "spring-verification-order")
    public void consumer(EmailVerificationToken message){
        emailService.send(
                message.to(),
                message.token()
        );
    }
//    @RabbitListener(queues = "spring-order-update")
//    public void consumer(OrderUpdateRabbitRequest orderMessageRabbitRequest){
//        emailService.sendOrderUpdateVerificationEmail(orderMessageRabbitRequest);
//    }





}
