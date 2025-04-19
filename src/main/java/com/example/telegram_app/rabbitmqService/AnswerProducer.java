package com.example.telegram_app.rabbitmqService;

import com.example.telegram_app.model.Answer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerProducer {

    private final RabbitTemplate rabbitTemplate;

    public void answer(String rabbitQueue, Answer answer) {
        rabbitTemplate.convertAndSend(rabbitQueue, answer);
    }
}
