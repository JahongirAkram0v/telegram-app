package com.example.telegram_app.model.dto;

import com.example.telegram_app.model.GroupState;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

    private Long groupId;
    private GroupState groupState;

    private List<PlayerDTO> playerDTOs;
}
