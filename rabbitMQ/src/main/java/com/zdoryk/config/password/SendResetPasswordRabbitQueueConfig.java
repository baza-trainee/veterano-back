package com.zdoryk.config.password;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendResetPasswordRabbitQueueConfig {

    public static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-reset-password";

    @Bean
    Queue resetPasswordQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange resetPasswordExchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding resetPasswordBinding(Queue resetPasswordQueue, TopicExchange resetPasswordExchange) {
        return BindingBuilder.bind(resetPasswordQueue).to(resetPasswordExchange).with("password.update");
    }
}
