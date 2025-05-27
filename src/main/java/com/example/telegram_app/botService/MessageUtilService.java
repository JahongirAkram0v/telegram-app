package com.example.telegram_app.botService;

import com.example.telegram_app.model.Answer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageUtilService {

    public Answer sendMessage(Long chatId, String text) {

        return new Answer(
                "sendMessage", Map.of(
                        "chat_id", chatId,
                        "text", text
                )
        );
    }

    public Answer sendMessage(Long chatId, String text, List<List<Map<String, Object>>> keyboard) {

        return new Answer("sendMessage", Map.of(
                "chat_id", chatId,
                "text", text,
                "reply_markup", Map.of("inline_keyboard", keyboard)
        ));
    }

    public Answer editMessageText(Integer messageId, Long chatId, String text) {

        return new Answer(
                "editMessageText", Map.of(
                        "message_id", messageId,
                        "chat_id", chatId,
                        "text", text
                )
        );
    }

    public Answer editMessageText(Integer messageId, Long chatId, String text, List<List<Map<String, Object>>> keyboard) {

        return new Answer(
                "editMessageText", Map.of(
                        "message_id", messageId,
                        "chat_id", chatId,
                        "text", text,
                        "reply_markup", Map.of("inline_keyboard", keyboard)
                )
        );
    }
}
