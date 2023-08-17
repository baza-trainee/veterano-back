package com.zdoryk.config.notification;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendSubscribeVerificationEmail {

    public static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-subscription-verification";

    @Bean
    Queue subscribeVerificationQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange subscribeVerificationExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding subscribeVerificationBinding(Queue subscribeVerificationQueue, TopicExchange subscribeVerificationExchange) {
        return BindingBuilder.bind(subscribeVerificationQueue).to(subscribeVerificationExchange).with("subscription.confirm");
    }
}

