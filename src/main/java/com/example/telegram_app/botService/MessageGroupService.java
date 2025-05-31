package com.example.telegram_app.botService;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class MessageGroupService {

    private final Dotenv dotenv = Dotenv.load();
    private final String telegramBotUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
    private final BotCommandService botCommandService;

    public void messageGroup(String rabbitQueue, Message message) {

        Long groupId = message.getChat().getId();
        String text = message.getText();

        if (text.equals("/start@"+telegramBotUsername)) {
            botCommandService.StartCommandGroup(rabbitQueue, groupId);
        }
    }
}
