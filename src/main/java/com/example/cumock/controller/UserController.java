package com.example.cumock.controller;

import com.example.cumock.dto.auth.UserResponse;
import com.example.cumock.model.User;
import com.example.cumock.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/me")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponse> getMyInfo() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername()
        ));
    }
}
