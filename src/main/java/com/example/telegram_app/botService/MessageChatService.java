package com.example.telegram_app.botService;

import com.example.telegram_app.model.Player;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.PlayerService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

import static com.example.telegram_app.model.UserState.*;

@Service
@RequiredArgsConstructor
public class MessageChatService {

    private final Dotenv dotenv = Dotenv.load();
    private final String telegramBotUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
    private final AnswerProducer answerProducer;
    private final PlayerService playerService;
    private final MessageUtilService messageUtilService;

    public void messageChat(String rabbitQueue, Message message) {

        Long chatId = message.getChatId();
        String text = message.getText();

        Player player = playerService.findById(chatId);

        // TODO: toliq sign up jarayonini keyinroq yozaman!
        if (player.getUserState().equals(SING_UP)) {

            player.setChatId(chatId);
            player.setUserState(START);
            playerService.save(player);

            String response = "Thank you for signing up! You can now start playing the game.";

            answerProducer.answer(
                    rabbitQueue,
                    messageUtilService.sendMessage(chatId, response)
            );
        }

        if (player.getUserState().equals(START) && text.equals("/start")) {
            String res = """
                    Welcome to the game!\s
                    To start playing, please add the bot to your group and make it an admin.\s
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
            return;
        }

        if (player.getUserState().equals(START) && text.equals("/info")) {
            String res = """
                    This is a game bot that allows you to play various games with your friends in a group chat.\s
                    To start playing, please add the bot to your group and make it an admin.\s
                    After that, you can start playing by sending the command /start.""";
            answerProducer.answer(
                    rabbitQueue,
                    messageUtilService.sendMessage(chatId, res)
            );
        }
    }
}
