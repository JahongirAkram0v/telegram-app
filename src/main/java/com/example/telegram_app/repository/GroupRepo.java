package com.example.telegram_app.repository;

import com.example.telegram_app.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRepo extends JpaRepository<Group, Long> {

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.players WHERE g.groupId = :groupId")
    Optional<Group> findByIdWithPlayers(@Param("groupId") Long groupId);
}
