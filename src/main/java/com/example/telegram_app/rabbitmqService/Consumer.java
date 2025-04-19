package com.example.telegram_app.rabbitmqService;

import com.example.telegram_app.botService.CallbackChatService;
import com.example.telegram_app.botService.MainService;
import com.example.telegram_app.botService.MessageChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.telegram_app.config.RabbitQueue.*;
import static com.example.telegram_app.config.RabbitQueue.ANSWER_QUEUE_GROUP;

@Service
@RequiredArgsConstructor
public class Consumer {

    private final MainService mainService;

    private final MessageChatService messageChatService;

    private final CallbackChatService callbackChatService;

    @RabbitListener(queues = MESSAGE_QUEUE_CHAT)
    public void messageChat(Message message) {
        messageChatService.messageChat(ANSWER_QUEUE_CHAT, message);
    }

    @RabbitListener(queues = MESSAGE_QUEUE_GROUP)
    public void messageGroup(Message message) {
        mainService.messageGroup(ANSWER_QUEUE_GROUP, message);
    }

    @RabbitListener(queues = CALLBACK_QUEUE_CHAT)
    public void callbackChat(CallbackQuery callbackQuery) {
        callbackChatService.callbackChat(ANSWER_QUEUE_CHAT, callbackQuery);
    }

    @RabbitListener(queues = CALLBACK_QUEUE_GROUP)
    public void callbackGroup(CallbackQuery callbackQuery) {
        mainService.callbackGroup(ANSWER_QUEUE_GROUP, callbackQuery);
    }

    @RabbitListener(queues = GROUP_QUEUE)
    public void group(ChatMemberUpdated chatMemberUpdated) {
        mainService.group(ANSWER_QUEUE_GROUP, chatMemberUpdated);
    }
}
