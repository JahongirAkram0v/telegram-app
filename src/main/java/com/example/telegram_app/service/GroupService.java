package com.example.telegram_app.service;

import com.example.telegram_app.model.Group;
import com.example.telegram_app.model.dto.GroupDTO;
import com.example.telegram_app.model.dto.PlayerDTO;
import com.example.telegram_app.repository.GroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepo groupRepo;

    public void save(Group group) {
        groupRepo.save(group);
    }

    public Optional<Group> findByGroupId(Long groupId) {
        return groupRepo.findById(groupId);
    }

    public Optional<Group> findGroupWithPlayers(Long groupId) {
        return groupRepo.findByIdWithPlayers(groupId);
    }

    public boolean existsByGroupId(Long groupId) {
        return groupRepo.existsById(groupId);
    }

    public GroupDTO groupToGroupDTO(Group group) {
        return GroupDTO.builder()
                .groupId(group.getGroupId())
                .groupState(group.getGroupState())
                .playerDTOs(group.getPlayers().stream()
                        .map(player -> PlayerDTO.builder()
                                .chatId(player.getChatId())
                                .isChoose(player.getChoosePlayerId() != null)
                                .build())
                        .toList())
                .build();
    }

    public GroupDTO updateGroupDTO(Long groupId) {
        return groupRepo.findByIdWithPlayers(groupId)
                .map(this::groupToGroupDTO)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
    }
}
