package com.example.telegram_app.botService;

import com.example.telegram_app.rabbitmqService.AnswerProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final MessageUtilService messageUtilService;
    private final AnswerProducer answerProducer;

    public void group(String rabbitQueue, ChatMemberUpdated chatMemberUpdated) {

        System.out.println("Chat ID: " + chatMemberUpdated.getChat().getId());
        System.out.println(chatMemberUpdated.getNewChatMember().getStatus());
        //TODO: qolgan statuslarni ham tekshirish kerak
        if (chatMemberUpdated.getNewChatMember().getStatus().equals("member")) {
            Long chatId = chatMemberUpdated.getChat().getId();

            answerProducer.answer(rabbitQueue, messageUtilService.sendMessage(chatId, "member"));
        } else if (chatMemberUpdated.getNewChatMember().getStatus().equals("administrator")) {
            Long chatId = chatMemberUpdated.getChat().getId();

            answerProducer.answer(rabbitQueue, messageUtilService.sendMessage(chatId, "administrator"));
        } else if (chatMemberUpdated.getNewChatMember().getStatus().equals("left")) {
            System.out.println("User left the group: " + chatMemberUpdated.getNewChatMember().getUser().getId());
        }
    }


}
