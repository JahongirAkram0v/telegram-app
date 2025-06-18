package com.example.telegram_app.repository;

import com.example.telegram_app.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepo extends JpaRepository<Group, Long> {
}
