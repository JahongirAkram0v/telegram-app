package com.example.telegram_app.rabbitmqService;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    public void message(String rabbitQueue, Message message) {
        rabbitTemplate.convertAndSend(rabbitQueue, message);
    }

    public void callback(String rabbitQueue, CallbackQuery callbackQuery) {
        rabbitTemplate.convertAndSend(rabbitQueue, callbackQuery);
    }

    public void group(String rabbitQueue, ChatMemberUpdated chatMemberUpdated) {
        rabbitTemplate.convertAndSend(rabbitQueue, chatMemberUpdated);
    }

}
