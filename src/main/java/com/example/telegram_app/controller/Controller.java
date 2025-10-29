package com.example.telegram_app.controller;

import com.example.telegram_app.botService.TelegramAuthService;
import com.example.telegram_app.model.Group;
import com.example.telegram_app.service.GroupService;
import com.example.telegram_app.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/verify-and-get-group")
@RequiredArgsConstructor
public class Controller {

    private final PlayerService playerService;
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<?> verifyUser(@RequestBody String initData) {

        boolean isValid = TelegramAuthService.verifyInitData(initData);

        if (!isValid) {
            return ResponseEntity.status(401).body("Invalid initData");
        }

        Long userId = TelegramAuthService.extractUserId(initData);
        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID not found");
        }

        System.out.println("Verified user ID: " + userId);

        Optional<Group> optionalGroup = playerService.findGroupByChatId(userId);

        if (optionalGroup.isEmpty()) {
            return ResponseEntity.status(404).body("User not found or not in a group");
        } else return ResponseEntity.ok(groupService.groupToGroupDTO(optionalGroup.get(), userId));

    }
}
