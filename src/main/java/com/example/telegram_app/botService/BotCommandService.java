package com.example.telegram_app.botService;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BotCommandService {

    private final Dotenv dotenv = Dotenv.load();
    private final String botUsername = dotenv.get("TELEGRAM_BOT_USERNAME");

    public final List<String> botCommand = Arrays.asList(
            "/start", "/info"
    );

    public boolean isBotCommand(String text) {
        return botCommand.stream().anyMatch(command -> command.startsWith(text));
    }

}
