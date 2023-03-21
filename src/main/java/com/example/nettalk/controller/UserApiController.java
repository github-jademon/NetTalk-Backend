package com.example.nettalk.controller;

import com.example.nettalk.dto.UserResponseDto;
import com.example.nettalk.config.SecurityUtil;
import com.example.nettalk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> findUserInfoById() {
        return ResponseEntity.ok(userService.findUserInfoById(SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> findUserInfoByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserInfoByEmail(email));
    }

}
