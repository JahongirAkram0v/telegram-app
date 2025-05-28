package com.example.telegram_app.repository;

import com.example.telegram_app.model.Groups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupsRepo extends JpaRepository<Groups, Long> {
    Optional<Groups> findByGroupId(Long groupId);
}
