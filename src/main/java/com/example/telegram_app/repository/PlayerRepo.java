package com.example.telegram_app.repository;

import com.example.telegram_app.model.Group;
import com.example.telegram_app.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlayerRepo extends JpaRepository<Player, Long> {

    @Query("SELECT p.group FROM Player p WHERE p.chatId = :chatId")
    Optional<Group> findGroupByChatId(Long chatId);
}
