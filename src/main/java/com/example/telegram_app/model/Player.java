package com.example.telegram_app.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
public class Player {

    @Id
    private Long chatId;

    @Enumerated(value = EnumType.STRING)
    private UserState userState = UserState.SIGN_UP;
    private Long choosePlayerId;
    private Boolean isActive = false;
    //
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Groups groups;
}
