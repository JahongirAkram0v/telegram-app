package com.example.telegram_app.service;

import com.example.telegram_app.model.Group;
import com.example.telegram_app.repository.GroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepo groupRepo;

    public void save(Group group) {
        groupRepo.save(group);
    }

    public Optional<Group> findByGroupId(Long groupId) {
        return groupRepo.findById(groupId);
    }
}
