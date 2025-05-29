package com.example.telegram_app.botService;

import com.example.telegram_app.model.Groups;
import com.example.telegram_app.model.Player;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.GroupsService;
import com.example.telegram_app.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;

import static com.example.telegram_app.config.RabbitQueue.ANSWER_QUEUE_GROUP;
import static com.example.telegram_app.model.UserState.*;

@Service
@RequiredArgsConstructor
public class MessageChatService {

    private final AnswerProducer answerProducer;
    private final MessageUtilService messageUtilService;
    private final PlayerService playerService;
    private final GroupsService groupsService;
    private final BotCommandService botCommandService;

    public void messageChat(String rabbitQueue, Message message) {

        Long chatId = message.getChatId();
        String firstName = message.getFrom().getFirstName();
        String text = message.getText();

        Player player = playerService.findById(chatId);

        // TODO: Link yoki referrallar uchun alohida class yarataman
        if (text.startsWith("/start -") && player.getUserState().equals(SIGN_UP)) {
            String temp = text.replace("/start -", "");
            if (temp.matches("\\d+")) {
                long groupId = Long.parseLong(temp);
                Groups groups = groupsService.findByGroupId(-1 * groupId);
                if (groups == null || groups.getGroupId() == null) {
                    System.out.println("Group not found with ID: -" + groupId);
                    return;
                }
                player.setChatId(chatId);
                player.setGroups(groups);
                player.setUserState(LINK_LANGUAGE);
                playerService.save(player);

            } else System.out.println("Invalid group ID format: " + temp);
            forLanguage(rabbitQueue, chatId);
            return;
        }

        // TODO: tilni keyinroq qoshaman
        if (player.getUserState().equals(SIGN_UP)) {
            player.setChatId(chatId);
            player.setUserState(LANGUAGE);
            playerService.save(player);
            forLanguage(rabbitQueue, chatId);
            return;
        }

        if (text.startsWith("/start -") && player.getUserState().equals(START)) {
            if (player.getGroups() != null) {
                System.out.println("Player already linked to a group: " + player.getGroups().getGroupId());
                return;
            }
            String temp = text.replace("/start -", "");
            if (temp.matches("\\d+")) {
                long groupId = Long.parseLong(temp);
                Groups groups = groupsService.findByGroupId(-1 * groupId);
                if (groups.getGroupId() == null) {
                    System.out.println("Group not found with ID: -" + groupId);
                    return;
                }
                player.setChatId(chatId);
                player.setGroups(groups);
                player.setUserState(JOINED);
                playerService.save(player);

                String response = "You have successfully joined the group with ID: " + groups.getGroupId();
                answerProducer.answer(
                        ANSWER_QUEUE_GROUP,
                        messageUtilService.sendMessage(chatId, response)
                );
                String groupResponse = firstName + " has joined";
                answerProducer.answer(
                        rabbitQueue,
                        messageUtilService.sendMessage(groups.getGroupId(), groupResponse)
                );

            } else System.out.println("Invalid group ID format: " + temp);
        }

        if (text.equals("/start")) {
            botCommandService.forStartCommand(rabbitQueue, chatId);
            return;
        }

        if (text.equals("/info")) {
            botCommandService.forInfoCommand(rabbitQueue, chatId);
        }
    }

    private void forLanguage(String rabbitQueue, Long chatId) {
        String response = "Choose a language";
        List<List<Map<String, Object>>> responseButtons = List.of(
                List.of(Map.of("text", "\uD83C\uDDFA\uD83C\uDDF8", "callback_data", "0"))
        );

        answerProducer.answer(
                rabbitQueue,
                messageUtilService.sendMessage(chatId, response, responseButtons)
        );
    }
}
