package com.example.telegram_app.service;

import com.example.telegram_app.model.Player;
import com.example.telegram_app.repository.PlayerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepo playerRepo;

    public void save(Player player) {
        playerRepo.save(player);
    }

    public Player findById(Long chatId) {
        return playerRepo.findById(chatId).orElse(new Player());
    }
}
