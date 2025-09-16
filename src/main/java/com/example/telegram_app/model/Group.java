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
@Entity
@Table(name = "_group")
public class Group {

    @Id
    private Long groupId;

    @Builder.Default
    private int language = 0; // 0 - en, 1 - uz, 2 - ru

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private GroupState groupState = GroupState.SIGN_UP;

    @Builder.Default
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderColumn(name = "player_index")
    private List<Player> players = new ArrayList<>();
}
