package com.example.nettalk.controller;

import com.example.nettalk.entity.room.Room;
import com.example.nettalk.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {
    private final RoomService roomService;

    @GetMapping("/hello")
    public String test() {
        return "Hello, world!";
    }

    @GetMapping("/rooms")
    public ResponseEntity LoadList() {
        try {
            List<Room> data = roomService.list();

            return new ResponseEntity(data, HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
