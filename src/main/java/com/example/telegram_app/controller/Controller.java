package com.example.telegram_app.controller;

import com.example.telegram_app.model.Group;
import com.example.telegram_app.model.Player;
import com.example.telegram_app.model.dto.ClientPlayerDTO;
import com.example.telegram_app.model.dto.GroupDTO;
import com.example.telegram_app.service.GroupService;
import com.example.telegram_app.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/player")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class Controller {

    private final PlayerService playerService;
    private final GroupService groupService;

    @GetMapping("/{playerId}")
    public ResponseEntity<ClientPlayerDTO> getByPlayerId(@PathVariable Long playerId) {

        Optional<Player> optionalPlayer = playerService.findById(playerId);
        return optionalPlayer
                .map(player -> ResponseEntity.ok(playerService.playerToClientPlayerDTO(player)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{groupId}/group")
    public ResponseEntity<GroupDTO> getByGroupId(@PathVariable Long groupId) {

        Optional<Group> optionalGroup = groupService.findByGroupId(groupId);
        return optionalGroup
                .map(group -> ResponseEntity.ok(groupService.groupToGroupDTO(group)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
