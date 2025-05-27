package com.example.telegram_app.repository;

import com.example.telegram_app.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepo extends JpaRepository<Player, Long> {
}
