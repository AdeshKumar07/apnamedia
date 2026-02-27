package com.example.CRUD.controller;

import com.example.CRUD.dto.UserResponseDto;
import com.example.CRUD.entity.User;
import com.example.CRUD.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class PendingFollowController {

    private final FollowService followService;

    @GetMapping("/pending")
    public List<UserResponseDto> pending(@AuthenticationPrincipal User user) {
        return followService.getPendingFollowers(user);
    }

    @PutMapping("/accept/{id}")
    public String accept(@PathVariable Long id, @AuthenticationPrincipal User user) {
        followService.acceptRequest(id, user);
        return "Accepted";
    }

    @PutMapping("/reject/{id}")
    public String reject(@PathVariable Long id, @AuthenticationPrincipal User user) {
        followService.rejectRequest(id, user);
        return "Rejected";
    }
}