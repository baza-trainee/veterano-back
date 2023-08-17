package com.zdoryk;

import com.zdoryk.config.notification.SendSubscribeVerificationEmail;
import com.zdoryk.config.notification.SendUpdatesForSubscribersEmail;
import com.zdoryk.config.password.SendMessageVerificationRabbitQueueConfig;
import com.zdoryk.config.password.SendResetPasswordRabbitQueueConfig;
import com.zdoryk.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class RabbitMQRunner {

    private final RabbitTemplate rabbitTemplate;


    public void sendNewCardsToSubscribers(List<ConsumerDto> consumers, List<CardToSendEmail> cards){
        log.info("sending verification email");
        rabbitTemplate.convertAndSend(
                SendUpdatesForSubscribersEmail.topicExchangeName,
                "subscription.update",
                new SubscriptionUpdateMessage(consumers,cards)
        );
    }

    public void sendSubscribeConfirmation(ConsumerDto consumerDto){
        log.info("sending verification email {}",consumerDto.getEmail());
        rabbitTemplate.convertAndSend(
                SendSubscribeVerificationEmail.topicExchangeName,
                "subscription.confirm",
                consumerDto
        );
    }


    public void sendEmailVerification(EmailVerificationToken message){
        log.info("sending verification email {}", message);
        rabbitTemplate.convertAndSend(
                SendMessageVerificationRabbitQueueConfig.topicExchangeName,
                "email.confirm",
                message
        );
    }

    public void sendEmailResetPasswordToken(EmailResetToken link) {
        log.info("sending reset password email");
        rabbitTemplate.convertAndSend(
                SendResetPasswordRabbitQueueConfig.topicExchangeName,
                "password.update",
                link
        );
    }

}