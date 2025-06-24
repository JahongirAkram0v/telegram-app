package com.example.telegram_app.model.dto;

import com.example.telegram_app.model.GroupState;
import com.example.telegram_app.model.PlayerState;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientPlayerDTO {

    private Long chatId;
    private PlayerState playerState;

    private Long groupId;
    private GroupState groupState;
}
