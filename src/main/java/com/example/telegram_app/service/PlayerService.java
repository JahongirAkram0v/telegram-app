package com.example.telegram_app.service;

import com.example.telegram_app.model.Player;
import com.example.telegram_app.model.dto.ControllerPlayerDTO;
import com.example.telegram_app.model.dto.ServerPlayerDTO;
import com.example.telegram_app.repository.PlayerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepo playerRepo;

    public void save(Player player) {
        playerRepo.save(player);
    }

    public Optional<Player> findById(Long chatId) {
        return playerRepo.findById(chatId);
    }

    public ControllerPlayerDTO playerToControllerPlayerDTO(Player player) {
        if (player.getGroup() == null) {
            return ControllerPlayerDTO.builder()
                    .chatId(player.getChatId())
                    .build();
        }
        return ControllerPlayerDTO.builder()
                .chatId(player.getChatId())
                .playerState(player.getPlayerState())
                .groupId(player.getGroup().getGroupId())
                .groupState(player.getGroup().getGroupState())
                .build();
    }

    public boolean existsByChatId(Long chatId) {
        return playerRepo.existsById(chatId);
    }

    public void updatePlayer(ServerPlayerDTO serverPlayerDTO) {
        Player player = playerRepo.findById(serverPlayerDTO.getChatId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        player.setPlayerState(serverPlayerDTO.getPlayerState());
        player.setChoosePlayerId(serverPlayerDTO.getChoosePlayerId());

        playerRepo.save(player);
    }
}
