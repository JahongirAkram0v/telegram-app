package com.example.telegram_app.botService;

import com.example.telegram_app.model.Groups;
import com.example.telegram_app.model.Player;
import com.example.telegram_app.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static com.example.telegram_app.model.UserState.LANGUAGE;
import static com.example.telegram_app.model.UserState.LINK_LANGUAGE;

@Service
@RequiredArgsConstructor
public class CallbackQueryChatService {

    private final PlayerService playerService;
    private final BotCommandService botCommandService;
    private final MessageChatService messageChatService;

    public void callbackChat(String rabbitQueue, CallbackQuery callbackQuery) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        String firstName = callbackQuery.getFrom().getFirstName();

        Player player = playerService.findById(chatId);

        if (player.getUserState().equals(LANGUAGE)) {
            System.out.println("Language selection callback received: " + callbackData);
            botCommandService.StartCommandChat(chatId, messageId);
        } else if (player.getUserState().equals(LINK_LANGUAGE)) {
            System.out.println("Link language selection callback received: " + callbackData);
            Groups groups = player.getGroups();
            messageChatService.handleGroupJoin(groups.getGroupId(), chatId, firstName);
        }
    }
}
