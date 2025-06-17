package com.example.telegram_app.botService;

import com.example.telegram_app.model.Groups;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.GroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

import static com.example.telegram_app.model.GroupState.LANGUAGE;
import static com.example.telegram_app.model.GroupState.SIGN_UP;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final MessageUtilService messageUtilService;
    private final AnswerProducer answerProducer;
    private final GroupsService groupsService;
    private final MessageChatService messageChatService;

    public void group(String rabbitQueue, ChatMemberUpdated chatMemberUpdated) {


        String status = chatMemberUpdated.getNewChatMember().getStatus();
        Long groupId = chatMemberUpdated.getChat().getId();
        System.out.println(status);
        System.out.println("Chat ID: " + groupId);

        //TODO: qolgan statuslarni ham tekshirish kerak

        switch (status) {
            case "member" -> {
                Groups groups = groupsService.findByGroupId(groupId);
                String text = "Make me an administrator to start the game!";
                answerProducer.answer(rabbitQueue, messageUtilService.sendMessage(groupId, text));
            }
            case "administrator" -> {

                Groups groups = groupsService.findByGroupId(groupId);
                if (groups.getGroupState().equals(SIGN_UP)) {
                    groups.setGroupId(groupId);
                    groups.setGroupState(LANGUAGE);
                    groupsService.save(groups);
                    messageChatService.forLanguage(rabbitQueue, groupId);
                    return;
                }
                String text = "O'yinni boshlash uchun /start ni bosing!";
                answerProducer.answer(rabbitQueue, messageUtilService.sendMessage(groupId, text));

            }
            case "kicked" -> System.out.println("User left the group: " + groupId);
        }
    }


}
