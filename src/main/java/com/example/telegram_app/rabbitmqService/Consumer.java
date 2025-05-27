package com.example.telegram_app.rabbitmqService;

import com.example.telegram_app.botService.GroupService;
import com.example.telegram_app.botService.MessageChatService;
import com.example.telegram_app.botService.MessageGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.telegram_app.config.RabbitQueue.*;

@Service
@RequiredArgsConstructor
public class Consumer {

    private final MessageChatService messageChatService;
    private final MessageGroupService messageGroupService;
    private final GroupService groupService;

    @RabbitListener(queues = MESSAGE_QUEUE_CHAT)
    public void messageChat(Message message) {
        messageChatService.messageChat(ANSWER_QUEUE_CHAT, message);
    }

    @RabbitListener(queues = MESSAGE_QUEUE_GROUP)
    public void messageGroup(Message message) {
        messageGroupService.messageGroup(ANSWER_QUEUE_GROUP, message);
    }

    @RabbitListener(queues = CALLBACK_QUEUE_CHAT)
    public void callbackChat(CallbackQuery callbackQuery) {

    }

    @RabbitListener(queues = CALLBACK_QUEUE_GROUP)
    public void callbackGroup(CallbackQuery callbackQuery) {

    }

    @RabbitListener(queues = GROUP_QUEUE)
    public void group(ChatMemberUpdated chatMemberUpdated) {
        groupService.group(ANSWER_QUEUE_GROUP, chatMemberUpdated);
    }
}
