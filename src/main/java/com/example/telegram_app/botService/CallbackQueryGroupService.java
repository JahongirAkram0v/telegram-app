package com.example.telegram_app.botService;

import com.example.telegram_app.model.Groups;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.GroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static com.example.telegram_app.model.GroupState.LANGUAGE;
import static com.example.telegram_app.model.GroupState.START;

@Service
@RequiredArgsConstructor
public class CallbackQueryGroupService {

    private final AnswerProducer answerProducer;
    private final MessageUtilService messageUtilService;
    private final GroupsService groupsService;

    public void callbackGroup(String rabbitmqQueue, CallbackQuery callbackQuery) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String callbackData = callbackQuery.getData();
        Long groupId = callbackQuery.getMessage().getChatId();

        Groups groups = groupsService.findByGroupId(groupId);

        if (groups.getGroupState().equals(LANGUAGE)) {
            groups.setGroupState(START);
            groupsService.save(groups);
            String text = "O'yinni boshlash uchun /start ni bosing!";

            answerProducer.answer(rabbitmqQueue,
                    messageUtilService.editMessageText(messageId, groupId, text + " -!- " + callbackData)
            );
        }


    }
}
