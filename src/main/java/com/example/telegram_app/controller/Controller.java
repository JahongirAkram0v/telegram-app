package com.example.telegram_app.controller;

import com.example.telegram_app.model.Group;
import com.example.telegram_app.model.Player;
import com.example.telegram_app.model.dto.ControllerPlayerDTO;
import com.example.telegram_app.model.dto.GroupDTO;
import com.example.telegram_app.service.GroupService;
import com.example.telegram_app.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/player")
@RequiredArgsConstructor
public class Controller {

    private final PlayerService playerService;
    private final GroupService groupService;

    @GetMapping("/{playerId}")
    public ResponseEntity<ControllerPlayerDTO> getByPlayerId(@PathVariable Long playerId) {
        System.out.println("salom");
        Optional<Player> optionalPlayer = playerService.findById(playerId);
        return optionalPlayer
                .map(player -> ResponseEntity.ok(playerService.playerToControllerPlayerDTO(player)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{groupId}/group")
    public ResponseEntity<GroupDTO> getByGroupId(@PathVariable Long groupId) {

        Optional<Group> optionalGroup = groupService.findGroupWithPlayers(groupId);
        return optionalGroup
                .map(group -> ResponseEntity.ok(groupService.groupToGroupDTO(group)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
