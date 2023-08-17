package com.zdoryk.config.password;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendMessageVerificationRabbitQueueConfig {

    public static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-verification-order";

    @Bean
    Queue messageVerificationQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange messageVerificationExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding messageVerificationBinding(Queue messageVerificationQueue, TopicExchange messageVerificationExchange) {
        return BindingBuilder.bind(messageVerificationQueue).to(messageVerificationExchange).with("email.confirm");
    }
}
