package com.example.telegram_app.model;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
public class Player {

    @Id
    private Long chatId;
    @Builder.Default
    private int languageCode = 0; // 0 - en, 1 - uz, 2 - ru

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private PlayerState playerState = PlayerState.SIGN_UP;
    private Long choosePlayerId;
    //
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
