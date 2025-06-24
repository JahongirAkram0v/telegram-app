package com.example.telegram_app.controller;

import com.example.telegram_app.model.Player;
import com.example.telegram_app.model.dto.ClientPlayerDTO;
import com.example.telegram_app.model.dto.ServerPlayerDTO;
import com.example.telegram_app.service.GroupService;
import com.example.telegram_app.service.PlayerService;
import com.rabbitmq.client.AMQP;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ClientController {

    private final SimpMessagingTemplate messagingTemplate;

    private final GroupService groupService;
    private final PlayerService playerService;

    @MessageMapping("/game.send")
    public void sendMessage(@Payload ServerPlayerDTO serverPlayerDTO) {

        if (!playerService.existsByChatId(serverPlayerDTO.getChatId())) {
            messagingTemplate.convertAndSend(
                    "/topic/room/" + serverPlayerDTO.getGroupId(),
                    "Player not found"
            );
            return;
        } else if (!groupService.existsByGroupId(serverPlayerDTO.getGroupId())){
            messagingTemplate.convertAndSend(
                    "/topic/room/" + serverPlayerDTO.getGroupId(),
                    "group does not exist"
            );
            return;
        }

        playerService.updatePlayer(serverPlayerDTO);

        messagingTemplate.convertAndSend(
                "/topic/room/" + serverPlayerDTO.getGroupId(),
                groupService.playerDTOToGroupDTO(serverPlayerDTO.getGroupId())
        );
    }

}
