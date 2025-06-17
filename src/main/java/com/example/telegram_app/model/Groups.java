package com.example.telegram_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
public class Groups {

    @Id
    private Long groupId;
    private int languageCode = 0; // 0 - en, 1 - uz, 2 - ru

    @Enumerated(EnumType.STRING)
    private GroupState groupState = GroupState.SIGN_UP;

    @OneToMany(mappedBy = "groups", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderColumn(name = "player_index")
    List<Player> players = new ArrayList<>();
}
