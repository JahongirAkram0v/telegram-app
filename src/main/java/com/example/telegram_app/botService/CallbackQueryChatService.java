package com.example.telegram_app.botService;

import com.example.telegram_app.model.Player;
import com.example.telegram_app.model.PlayerState;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Optional;

import static com.example.telegram_app.config.RabbitQueue.ANSWER_QUEUE_GROUP;
import static com.example.telegram_app.model.PlayerState.*;

@Service
@RequiredArgsConstructor
public class CallbackQueryChatService {

    private final PlayerService playerService;
    private final BotCommandService botCommandService;
    private final MessageUtilService messageUtilService;
    private final AnswerProducer answerProducer;

    public void callbackChat(String rabbitQueue, CallbackQuery callbackQuery) {
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String callbackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();
        String firstName = callbackQuery.getFrom().getFirstName();

        Optional<Player> optionalPlayer = playerService.findById(chatId);
        if (optionalPlayer.isEmpty()) return;
        Player player = optionalPlayer.get();
        PlayerState state = player.getPlayerState();

        switch (state) {
            case LANGUAGE -> {
                System.out.println("Language selection callback received: " + callbackData);
                player.setPlayerState(START);
                playerService.save(player);
                botCommandService.StartCommandChat(chatId, messageId);
            }
            case LINK_LANGUAGE -> {

                System.out.println("Link language selection callback received: " + callbackData);
                player.setPlayerState(IN_GAME);
                playerService.save(player);
                Long groupId = player.getGroup().getGroupId();
                String response = "You have successfully joined the group with ID: " + groupId;
                answerProducer.answer(
                        rabbitQueue,
                        messageUtilService.editMessageText(messageId, chatId, response)
                );
                String groupResponse = firstName + " has joined";
                answerProducer.answer(
                        ANSWER_QUEUE_GROUP,
                        messageUtilService.sendMessage(groupId, groupResponse)
                );
            }
        }
    }
}
