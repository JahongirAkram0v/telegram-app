package com.example.telegram_app.service;

import com.example.telegram_app.model.Groups;
import com.example.telegram_app.repository.GroupsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupsService {

    private final GroupsRepo groupRepo;

    public void save(Groups groups) {
        groupRepo.save(groups);
    }

    public Groups findByGroupId(Long groupId) {
        return groupRepo.findByGroupId(groupId).orElse(new Groups());
    }
}
