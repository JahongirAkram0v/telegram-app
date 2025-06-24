package com.example.telegram_app.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {

    private Long chatId;
    private boolean isChoose;
}
