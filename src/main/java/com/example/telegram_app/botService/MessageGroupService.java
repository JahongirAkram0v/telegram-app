package com.example.telegram_app.botService;

import com.example.telegram_app.model.Player;
import com.example.telegram_app.rabbitmqService.AnswerProducer;
import com.example.telegram_app.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.telegram_app.model.UserState.*;

@Service
@RequiredArgsConstructor
public class MessageGroupService {

    private final AnswerProducer answerProducer;
    private final PlayerService playerService;
    private final MessageUtilService messageUtilService;

    public void messageGroup(String rabbitQueue, Message message) {

        Long groupId = message.getChat().getId();
        Long chatId = message.getFrom().getId();
        String text = message.getText();

        Player player = playerService.findById(chatId);

        if (player.getUserState().equals(SING_UP)) {
            player.setChatId(chatId);
            player.setUserState(START);
            playerService.save(player);
            System.out.println("Player signed up: " + player.getChatId());
        }
        if (player.getUserState().equals(START) && text.contains("/start")) {
            String response = """
                    Welcome to the game!""";

            answerProducer.answer(
                    rabbitQueue,
                    messageUtilService.sendMessage(groupId, response)
            );
        }
    }
}
