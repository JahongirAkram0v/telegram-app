package com.example.telegram_app.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity()
public class Groups {

    @Id
    private Long groupId;

    @OneToMany(mappedBy = "groups", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderColumn(name = "player_index")
    List<Player> players = new ArrayList<>();
}
