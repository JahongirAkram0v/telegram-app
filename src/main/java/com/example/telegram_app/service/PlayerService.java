package com.example.telegram_app.service;

import com.example.telegram_app.model.Player;
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
}
