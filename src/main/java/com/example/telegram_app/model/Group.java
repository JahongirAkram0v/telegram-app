package com.example.telegram_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "_group")
public class Group {

    @Id
    private Long groupId;
    private int languageCode = 0; // 0 - en, 1 - uz, 2 - ru

    @Enumerated(EnumType.STRING)
    private GroupState groupState = GroupState.SIGN_UP;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderColumn(name = "player_index")
    List<Player> players = new ArrayList<>();
}
