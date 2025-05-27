package com.example.telegram_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
public class Player {

    @Id
    private Long chatId;

    @Enumerated(value = EnumType.STRING)
    private UserState userState = UserState.SING_UP;
}
