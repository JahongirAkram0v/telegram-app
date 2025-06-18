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
    private int languageCode = 0; // 0 - en, 1 - uz, 2 - ru

    @Enumerated(value = EnumType.STRING)
    private UserState userState = UserState.SIGN_UP;
    private Long choosePlayerId;
    private Boolean isActive = false;
    //
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
