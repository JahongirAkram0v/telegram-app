package com.example.telegram_app.botService;

import com.example.telegram_app.model.Group;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.GroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.telegram_app.model.GroupState.LANGUAGE;
import static com.example.telegram_app.model.GroupState.SIGN_UP;

@Service
@RequiredArgsConstructor
public class GService {

    private final MessageUtilService messageUtilService;
    private final AnswerProducer answerProducer;
    private final GroupsService groupsService;

    public void group(String rabbitQueue, ChatMemberUpdated chatMemberUpdated) {


        String status = chatMemberUpdated.getNewChatMember().getStatus();
        Long groupId = chatMemberUpdated.getChat().getId();
        System.out.println(status);
        System.out.println("Chat ID: " + groupId);

        //TODO: qolgan statuslarni ham tekshirish kerak

        switch (status) {
            case "member" -> {
                String text = "Make me an administrator to start the game!";
                answerProducer.answer(rabbitQueue, messageUtilService.sendMessage(groupId, text));
            }
            case "administrator" -> {

                Optional<Group> optionalGroup = groupsService.findByGroupId(groupId);
                Group group = optionalGroup.orElseGet(Group::new);

                if (group.getGroupState().equals(SIGN_UP)) {
                    group.setGroupId(groupId);
                    group.setGroupState(LANGUAGE);
                    groupsService.save(group);
                    String response = "Choose a language";
                    List<List<Map<String, Object>>> responseButtons = List.of(
                            List.of(Map.of("text", "\uD83C\uDDFA\uD83C\uDDF8", "callback_data", "0"))
                    );

                    answerProducer.answer(
                            rabbitQueue,
                            messageUtilService.sendMessage(groupId, response, responseButtons)
                    );
                    return;
                }
                String text = "O'yinni boshlash uchun /start ni bosing!";
                answerProducer.answer(rabbitQueue, messageUtilService.sendMessage(groupId, text));

            }
            case "kicked" -> System.out.println("User left the group: " + groupId);
        }
    }


}
