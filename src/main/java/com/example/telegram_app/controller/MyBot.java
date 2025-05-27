package com.example.telegram_app.controller;

import com.example.telegram_app.botService.BotCommandService;
import com.example.telegram_app.rabbitmqService.Producer;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.*;

import static com.example.telegram_app.config.RabbitQueue.*;


@Component
@RequiredArgsConstructor
public class MyBot extends TelegramWebhookBot {

    private final Dotenv dotenv = Dotenv.load();
    private final String botUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
    private final String botWebhookPath = dotenv.get("TELEGRAM_BOT_WEBHOOK_PATH");

    private final Producer producer;
    private final BotCommandService botCommandService;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        // TODO
        if (update.hasMyChatMember()) {
            producer.group(GROUP_QUEUE, update.getMyChatMember());
            return null;
        }

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            MaybeInaccessibleMessage message = callbackQuery.getMessage();

            if (message.isGroupMessage() || message.isSuperGroupMessage()) {
                producer.callback(CALLBACK_QUEUE_GROUP, callbackQuery);
            } else if (message.isUserMessage()){
                producer.callback(CALLBACK_QUEUE_CHAT, callbackQuery);
            } else {
                System.out.println("CallbackQuery: Message is not a group or user " + message);
            }

            return null;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {

            Message message = update.getMessage();
            if (!botCommandService.isBotCommand(message.getText())) {
                return null;
            }
            Chat chat = message.getChat();

            if (chat.isSuperGroupChat() || chat.isGroupChat()) {
                producer.message(MESSAGE_QUEUE_GROUP, message);
            } else if (chat.isUserChat()) {
                producer.message(MESSAGE_QUEUE_CHAT, message);
            } else {
                System.out.println("Message: Chat is not a group or user chat " + chat);
            }

            return null;
        }

        return null;
    }

    @Override
    public String getBotPath() {
        return botWebhookPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}
