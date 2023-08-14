package com.zdoryk.rabbitmq;

import com.zdoryk.email.EmailService;
import com.zdoryk.util.EmailResetToken;
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
        emailService.sendVerificationEmail(
                message.to(),
                message.link()
        );
    }

    @RabbitListener(queues = "spring-reset-password")
    public void consumer(EmailResetToken emailResetToken){
        emailService.sendResetPasswordEmail(emailResetToken.to(), emailResetToken.link());
    }





}
