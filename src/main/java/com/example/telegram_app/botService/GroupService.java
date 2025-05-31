package com.example.telegram_app.botService;

import com.example.telegram_app.model.Groups;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.GroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

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

        if (status.equals("member")) {
            String text = "Meni admin!";
            answerProducer.answer(rabbitQueue, messageUtilService.sendMessage(groupId, text));

        } else if (status.equals("administrator")) {

            Groups groups = groupsService.findByGroupId(groupId);
            if (groups.getGroupId() == null) {
                groups.setGroupId(groupId);
                groupsService.save(groups);
                messageChatService.forLanguage(rabbitQueue, groupId);
                return;
            }
            String text = "You are now an administrator of the group!";
            answerProducer.answer(rabbitQueue, messageUtilService.sendMessage(groupId, text));

        } else if (status.equals("kicked")){
            System.out.println("User left the group: " + groupId);
        }
    }


}
