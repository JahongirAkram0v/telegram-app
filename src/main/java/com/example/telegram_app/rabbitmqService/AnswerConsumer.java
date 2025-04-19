package com.example.telegram_app.rabbitmqService;

import com.example.telegram_app.botService.SendService;
import com.example.telegram_app.model.Answer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.example.telegram_app.config.RabbitQueue.ANSWER_QUEUE_CHAT;
import static com.example.telegram_app.config.RabbitQueue.ANSWER_QUEUE_GROUP;

@Service
@RequiredArgsConstructor
public class AnswerConsumer {

    private final SendService sendService;

    @RabbitListener(queues = ANSWER_QUEUE_CHAT)
    public void answerChat(Answer answer) {

        //Todo: Optimize this code
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        sendService.send(answer.body(), answer.method());

    }

    @RabbitListener(queues = ANSWER_QUEUE_GROUP)
    public void answerGroup(Answer  answer) {

        // Todo: Optimize this code
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        sendService.send(answer.body(), answer.method());
    }
}
