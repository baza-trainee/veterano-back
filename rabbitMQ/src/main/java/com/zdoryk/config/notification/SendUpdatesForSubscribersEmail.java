package com.zdoryk.config.notification;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendUpdatesForSubscribersEmail {

    public static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-subscription-new-card";

    @Bean
    Queue subscribeUpdatesQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange subscribeUpdatesExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding subscribeUpdatesBinding(Queue subscribeUpdatesQueue, TopicExchange subscribeUpdatesExchange) {
        return BindingBuilder.bind(subscribeUpdatesQueue).to(subscribeUpdatesExchange).with("subscription.update");
    }
}

