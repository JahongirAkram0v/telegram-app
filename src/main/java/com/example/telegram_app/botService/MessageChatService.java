package com.example.telegram_app.botService;

import com.example.telegram_app.model.Group;
import com.example.telegram_app.model.Player;
import com.example.telegram_app.model.UserState;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.GroupsService;
import com.example.telegram_app.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.telegram_app.config.RabbitQueue.ANSWER_QUEUE_GROUP;
import static com.example.telegram_app.model.UserState.*;
import static com.example.telegram_app.model.GroupState.JOIN;

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

        Optional<Player> optionalPlayer = playerService.findById(chatId);
        Player player = optionalPlayer.orElseGet(Player::new);
        UserState state = player.getUserState();

        switch (state) {
            case SIGN_UP -> {
                if (text.startsWith("/start -")) {
                    String temp = text.replace("/start -", "");
                    CheckRefAndJoin(rabbitQueue, player, chatId, firstName, temp, state);
                } else {
                    initializePlayer(rabbitQueue, player, chatId, LANGUAGE);
                }
            }
            case START -> {
                if (text.startsWith("/start -")) {
                    String temp = text.replace("/start -", "");
                    CheckRefAndJoin(rabbitQueue, player, chatId, firstName, temp, state);
                }
                else if (text.equals("/start")) {
                    botCommandService.StartCommandChat(chatId);
                }
                else if (text.equals("/info")) {
                    botCommandService.InfoCommandChat(chatId);
                }
            }
            case LANGUAGE, LINK_LANGUAGE -> {
                System.out.println("User is stupid" + chatId + " " + firstName);
                if (text.startsWith("/start -")) {
                    String temp = text.replace("/start -", "");
                    CheckRefAndJoin(rabbitQueue, player, chatId, firstName, temp, SIGN_UP);
                } else {
                    initializePlayer(rabbitQueue, player, chatId, LANGUAGE);
                }
            }
            case JOINED -> webAppButton(rabbitQueue, chatId);
        }
    }

    private void CheckRefAndJoin(
            String rabbitQueue, Player player, Long chatId, String firstName,
            String temp, UserState state
    ) {

        if (state == LANGUAGE || state == LINK_LANGUAGE) {
            initializePlayer(rabbitQueue, player, chatId, state);
            return;
        }

        if (state == START && player.getGroup() != null) {
            System.out.println("Player already linked to a group: " + player.getGroup().getGroupId());
            return;
        }

        if (temp.matches("\\d+")) {
            long groupId = -1 * Long.parseLong(temp);

            Optional<Group> optionalGroup = groupsService.findByGroupId(groupId);
            //TODO: userga xabar yuborsam ham boladi
            if (optionalGroup.isEmpty()) {
                System.out.println("Group not found with ID: " + groupId);
                if (state == SIGN_UP) {
                    initializePlayer(rabbitQueue, player, chatId, LANGUAGE);
                }
                return;
            }
            Group group = optionalGroup.get();

            //TODO: userga xabar yuborsam ham boladi
            if (!group.getGroupState().equals(JOIN)) {
                System.out.println("Group is not in JOINED state: " + group.getGroupState());
                if (state == SIGN_UP) {
                    initializePlayer(rabbitQueue, player, chatId, LANGUAGE);
                }
                return;
            }

            if (state == SIGN_UP) {
                player.setChatId(chatId);
            }
            player.setUserState(JOINED);
            player.setGroup(group);
            // TODO: keyinchalik kerakli tekshirishlar ishlataman
            group.getPlayers().add(player);
            groupsService.save(group);

            if (state == START) {
                webAppButton(rabbitQueue, chatId);

                String groupResponse = firstName + " has joined";
                answerProducer.answer(
                        ANSWER_QUEUE_GROUP,
                        messageUtilService.sendMessage(groupId, groupResponse)
                );
            }
            if (state == SIGN_UP) {
                initializePlayer(rabbitQueue, player, chatId, LINK_LANGUAGE);
            }
        } else {
            System.out.println("Invalid group ID format: " + temp);
            if (state == SIGN_UP) {
                initializePlayer(rabbitQueue, player, chatId, LANGUAGE);
            }
        }

    }

    private void webAppButton(String rabbitQueue, Long chatId) {
        String response = "You have successfully joined the group";
        answerProducer.answer(
                rabbitQueue,
                messageUtilService.sendMessage(chatId, response)
        );
    }

    private void initializePlayer(String rabbitQueue, Player player, Long chatId, UserState userState) {
        player.setChatId(chatId);
        player.setUserState(userState);
        playerService.save(player);

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