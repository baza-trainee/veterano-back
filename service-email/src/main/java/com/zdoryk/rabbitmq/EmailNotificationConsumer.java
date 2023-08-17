package com.zdoryk.rabbitmq;

import com.zdoryk.config.notification.SendUpdatesForSubscribersEmail;
import com.zdoryk.email.EmailService;
import com.zdoryk.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EmailNotificationConsumer {

    private final EmailService emailService;


    @RabbitListener(queues = "spring-subscription-new-card")
    public void consumer(SubscriptionUpdateMessage subscriptionUpdateMessage){
        List<ConsumerDto> subscribers = subscriptionUpdateMessage.getConsumers();
        List<CardToSendEmail> cards = subscriptionUpdateMessage.getCards();

        cards.forEach(card -> {
            subscribers.forEach(subscriber -> {
                emailService.sendNewProjects(
                        subscriber.getEmail(),
                        subscriber.getName(),
                        card);
            });
        });

    }

    @RabbitListener(queues = "spring-subscription-verification")
    public void consumer(ConsumerDto consumerDto){
        emailService.sendNotificationVerificationEmail(
                consumerDto.getEmail(),
                consumerDto.getName()
        );
    }


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
