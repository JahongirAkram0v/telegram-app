package com.example.telegram_app.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.telegram_app.config.RabbitQueue.*;

@Configuration
public class RabbitmqConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue messageQueueChat() {
        return new Queue(MESSAGE_QUEUE_CHAT);
    }

    @Bean
    public Queue messageQueueGroup() {
        return new Queue(MESSAGE_QUEUE_GROUP);
    }

    @Bean
    public Queue callbackQueueChat() {
        return new Queue(CALLBACK_QUEUE_CHAT);
    }

    @Bean
    public Queue callbackQueueGroup() {
        return new Queue(CALLBACK_QUEUE_GROUP);
    }

    // TODO
    @Bean
    public Queue groupQueue() {
        return new Queue(GROUP_QUEUE);
    }

    @Bean
    public Queue answerQueueChat() {
        return new Queue(ANSWER_QUEUE_CHAT);
    }

    @Bean
    public Queue answerQueueGroup() {
        return new Queue(ANSWER_QUEUE_GROUP);
    }

}
