package com.example.telegram_app.model.dto;

import com.example.telegram_app.model.GroupState;
import com.example.telegram_app.model.PlayerState;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ControllerPlayerDTO {


    private Long chatId;
    private Long groupId;
    //groupState orqali group oyinga tayyor yoki boshlangani bilsa boladi
    private GroupState groupState;
    //playerState orqali player oyinga qoshilganini bilsa boladi
    private PlayerState playerState;


}
