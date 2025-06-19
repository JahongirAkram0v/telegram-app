package com.example.telegram_app.botService;

import com.example.telegram_app.model.Group;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Optional;

import static com.example.telegram_app.model.GroupState.LANGUAGE;
import static com.example.telegram_app.model.GroupState.START;

@Service
@RequiredArgsConstructor
public class CallbackQueryGroupService {

    private final AnswerProducer answerProducer;
    private final MessageUtilService messageUtilService;
    private final GroupService groupService;

    public void callbackGroup(String rabbitmqQueue, CallbackQuery callbackQuery) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String callbackData = callbackQuery.getData();
        Long groupId = callbackQuery.getMessage().getChatId();

        Optional<Group> optionalGroup = groupService.findByGroupId(groupId);
        if (optionalGroup.isEmpty()) return;
        Group group = optionalGroup.get();

        if (group.getGroupState().equals(LANGUAGE)) {
            group.setGroupState(START);
            groupService.save(group);
            String text = "O'yinni boshlash uchun /start ni bosing!";

            answerProducer.answer(rabbitmqQueue,
                    messageUtilService.editMessageText(messageId, groupId, text + " -!- " + callbackData)
            );
        }


    }
}
