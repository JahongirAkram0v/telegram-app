package com.example.telegram_app.botService;

import com.example.telegram_app.model.Groups;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.GroupsService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

import static com.example.telegram_app.config.RabbitQueue.ANSWER_QUEUE_GROUP;
import static com.example.telegram_app.model.GroupState.*;

@Service
@RequiredArgsConstructor
public class MessageGroupService {

    private final Dotenv dotenv = Dotenv.load();
    private final String telegramBotUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
    private final AnswerProducer answerProducer;
    private final MessageUtilService messageUtilService;
    private final GroupsService groupsService;

    public void messageGroup(String rabbitQueue, Message message) {

        Long groupId = message.getChat().getId();
        String text = message.getText();
        Groups groups = groupsService.findByGroupId(groupId);

        if (groups.getGroupState().equals(SIGN_UP) || groups.getGroupState().equals(LANGUAGE)) {
            return;
        }

        if (groups.getGroupState().equals(START) && text.equals("/start@"+telegramBotUsername)) {

            groups.setGroupState(JOIN);
            groupsService.save(groups);

            List<List<Map<String, Object>>> response = List.of(
                    List.of(Map.of(
                            "text", "Play",
                            "url", "https://t.me/" + telegramBotUsername + "?start=" + groupId))
            );
            String responseText = "salom";
            answerProducer.answer(
                    ANSWER_QUEUE_GROUP,
                    messageUtilService.sendMessage(groupId, responseText, response)
            );

        } else if (groups.getGroupState().equals(START) && text.equals("/info@"+telegramBotUsername)) {

            String res = """
                    dad""";
            answerProducer.answer(
                    ANSWER_QUEUE_GROUP,
                    messageUtilService.sendMessage(groupId, res)
            );

        }
    }
}
