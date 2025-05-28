package com.example.telegram_app.botService;

import com.example.telegram_app.model.Groups;
import com.example.telegram_app.model.Player;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.GroupsService;
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
    private final MessageUtilService messageUtilService;
    private final PlayerService playerService;
    private final GroupsService groupsService;
    public void messageChat(String rabbitQueue, Message message) {

        Long chatId = message.getChatId();
        String firstName = message.getFrom().getFirstName();
        String text = message.getText();

        Player player = playerService.findById(chatId);

        // TODO: toliq sign up jarayonini keyinroq yozaman!
        if (player.getUserState().equals(SIGN_UP)) {

            player.setChatId(chatId);
            player.setUserState(START);
            playerService.save(player);
            //TODO: callback uchun sozlashim kerak.
            String response = "Thank you for signing up! You can now start playing the game.";

            answerProducer.answer(
                    rabbitQueue,
                    messageUtilService.sendMessage(chatId, response)
            );
        }

        // todo: u bu narsa qusharman
        if (text.startsWith("/start -")) {
            String temp = text.replace("/start -", "");

            if (temp.matches("\\d+")) {
                long groupId = Long.parseLong(temp);
                Groups groups = groupsService.findByGroupId(-1 * groupId);
                if (groups.getGroupId() == null) {
                    System.out.println("Group not found with ID: " + groupId);
                    return;
                }
                player.setGroups(groups);
                player.setUserState(JOINED);
                playerService.save(player);

                String response = "You have successfully joined the group with ID: " + groups.getGroupId();
                answerProducer.answer(
                        rabbitQueue,
                        messageUtilService.sendMessage(chatId, response)
                );
                String groupResponse = firstName + " has joined";
                answerProducer.answer(
                        rabbitQueue,
                        messageUtilService.sendMessage(groups.getGroupId(), groupResponse)
                );
            }
        }

        if (text.equals("/start")) {
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
            return;
        }

        if (text.equals("/info")) {
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
}
