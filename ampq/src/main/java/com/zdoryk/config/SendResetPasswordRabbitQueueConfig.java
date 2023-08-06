package com.zdoryk.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendResetPasswordRabbitQueueConfig {


    public static final String topicExchangeName = "spring-boot-exchange";

    public static final String queueName = "spring-reset-password";

    @Bean
    Queue queue2() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange2() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding2(Queue queue2, TopicExchange exchange2) {
        return BindingBuilder.bind(queue2).to(exchange2).with("internal.update");
    }


}
