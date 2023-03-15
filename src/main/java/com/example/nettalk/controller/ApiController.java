package com.example.nettalk.controller;

import com.example.nettalk.entity.UserEntity;
import com.example.nettalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/api")
@RestController
public class ApiController {
    UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/hello")
    public String test() {
        return "Hello, world!";
    }



}
