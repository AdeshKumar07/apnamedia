package com.example.CRUD.controller;

import com.example.CRUD.dto.UserResponseDto;
import com.example.CRUD.entity.User;
import com.example.CRUD.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class MutualFriendController {

    private final FollowService followService;

    @GetMapping("/mutual-friends/{userId}")
    public List<UserResponseDto> mutual(@PathVariable Long userId,
                                        @AuthenticationPrincipal User user) {
        return followService.getMutualFriends(userId, user);
    }
}