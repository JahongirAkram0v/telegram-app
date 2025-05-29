package com.example.telegram_app.botService;

import com.example.telegram_app.rabbitmqService.AnswerProducer;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BotCommandService {

    private final Dotenv dotenv = Dotenv.load();
    private final String telegramBotUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
    private final AnswerProducer answerProducer;
    private final MessageUtilService messageUtilService;

    public final List<String> botCommand = Arrays.asList(
            "/start", "/info"
    );

    public boolean isBotCommand(String text) {
        return botCommand.stream().anyMatch(command -> command.startsWith(text));
    }

    public void forStartCommand(String rabbitQueue, Long chatId) {
        String res = """
                Welcome to the game!\s
                To start playing, please add the bot to your groups and make it an admin.\s
                After that, you can start playing by sending the command /start.""";

        List<List<Map<String, Object>>> response = List.of(
                List.of(Map.of(
                        "text", "Add Bot",
                        "url", "https://t.me/" + telegramBotUsername + "?startgroup=true"))
        );

        answerProducer.answer(
                rabbitQueue,
                messageUtilService.sendMessage(chatId, res, response)
        );
    }

    public void forInfoCommand(String rabbitQueue, Long chatId) {
        String res = """
                    This is a game bot that allows you to play various games with your friends in a groups chat.\s
                    To start playing, please add the bot to your groups and make it an admin.\s
                    After that, you can start playing by sending the command /start.""";
        answerProducer.answer(
                rabbitQueue,
                messageUtilService.sendMessage(chatId, res)
        );
    }

}
