package com.example.telegram_app.controller;

import com.example.telegram_app.botService.TelegramAuthService;
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

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody String initData) {

        System.out.println(initData);

        boolean isValid = TelegramAuthService.verifyInitData(initData);

        if (!isValid) {
            return ResponseEntity.status(401).body("Invalid initData");
        }

        Long userId = TelegramAuthService.extractUserId(initData);
        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID not found");
        }

        Optional<Player> optionalPlayer = playerService.findById(userId);

        if (optionalPlayer.isPresent()) {
            ControllerPlayerDTO playerDTO = playerService.playerToControllerPlayerDTO(optionalPlayer.get());
            return ResponseEntity.ok(playerDTO);
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

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
