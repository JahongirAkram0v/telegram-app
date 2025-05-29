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
        // todo ; buni groupga ko'chirishim kerak
        if (groups.getGroupId() == null) {
            groups.setGroupId(groupId);
            groupsService.save(groups);
        }

        if (text.equals("/start@"+telegramBotUsername)) {
            List<List<Map<String, Object>>> response = List.of(
                    List.of(Map.of(
                            "text", "Play",
                            "url", "https://t.me/" + telegramBotUsername + "?start=" + groups.getGroupId()))
            );
            String responseText = "salom";
            answerProducer.answer(
                   rabbitQueue,
                   messageUtilService.sendMessage(groupId, responseText, response)
            );
        }
    }
}
