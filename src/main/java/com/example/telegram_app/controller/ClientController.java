package com.example.telegram_app.controller;

import com.example.telegram_app.model.Group;
import com.example.telegram_app.model.Player;
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
public class ClientController {

    private final GroupService groupService;
    private final PlayerService playerService;

    @GetMapping("/{playerId}")
    public ResponseEntity<Player> getById(@PathVariable Long playerId) {
        Optional<Player> optionalPlayer = playerService.findById(playerId);
        return optionalPlayer
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping()
    public ResponseEntity<Void> savePlayer(@RequestBody Player player) {
        playerService.save(player);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{playerId}/group")
    public ResponseEntity<Group> getByPlayerId(@PathVariable Long playerId) {

        Optional<Player> optionalPlayer = playerService.findById(playerId);
        return optionalPlayer
                .map(player -> ResponseEntity.ok(player.getGroup()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/group")
    public ResponseEntity<Void> saveGroup(@RequestBody Group group) {
        groupService.save(group);
        return ResponseEntity.ok().build();
    }

}
