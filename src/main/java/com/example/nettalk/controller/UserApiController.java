package com.example.nettalk.controller;

import com.example.nettalk.entity.UserEntity;
import com.example.nettalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {

    @Autowired
    private UserService userService;

    @PostMapping("/add/user")
    public UserEntity signup(@RequestBody UserEntity req) {
        UserEntity userEntity = UserEntity.builder()
                .email(req.getEmail())
                .userid(req.getUserid())
                .password(userService.encode(req.getPassword()))
                .build();

        return userService.insert(userEntity);
    }

}
