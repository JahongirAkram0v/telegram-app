package com.example.telegram_app.botService;

import com.example.telegram_app.model.Group;
import com.example.telegram_app.model.GroupState;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.GroupService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.telegram_app.model.GroupState.*;

@Service
@RequiredArgsConstructor
public class MessageGroupService {

    private final Dotenv dotenv = Dotenv.load();
    private final String telegramBotUsername = dotenv.get("TELEGRAM_BOT_USERNAME");
    private final AnswerProducer answerProducer;
    private final MessageUtilService messageUtilService;
    private final GroupService groupService;

    public void messageGroup(String rabbitQueue, Message message) {

        Long groupId = message.getChat().getId();
        String text = message.getText();

        Optional<Group> optionalGroup = groupService.findByGroupId(groupId);
        if (optionalGroup.isEmpty()) {
            System.out.println("ERROR!! --- Group not found for ID: " + groupId);
            return;
        }
        Group group = optionalGroup.get();
        GroupState groupState = group.getGroupState();

        if (groupState == SIGN_UP || groupState == LANGUAGE) {
            return;
        }

        switch (groupState) {
            case START -> {
                if (text.startsWith("/start@")) {
                    group.setGroupState(JOIN);
                    groupService.save(group);

                    List<List<Map<String, Object>>> response = List.of(
                            List.of(Map.of(
                                    "text", "Play",
                                    "url", "https://t.me/" + telegramBotUsername + "?start=" + groupId))
                    );
                    String responseText = "salom";
                    answerProducer.answer(
                            rabbitQueue,
                            messageUtilService.sendMessage(groupId, responseText, response)
                    );
                }
                else if (text.startsWith("/info@")) {
                    String res = "dad";
                    answerProducer.answer(
                            rabbitQueue,
                            messageUtilService.sendMessage(groupId, res)
                    );
                }
            }
            case JOIN -> {
                if (text.startsWith("/start@")) {
                    group.setGroupState(GAME);
                    groupService.save(group);

                    String res = "Oyin boshlandi!";
                    answerProducer.answer(
                            rabbitQueue,
                            messageUtilService.sendMessage(groupId, res)
                    );
                }
            }
        }

    }
}
