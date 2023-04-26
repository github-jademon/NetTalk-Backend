package com.example.nettalk.controller;

import com.example.nettalk.config.SecurityUtil;
import com.example.nettalk.dto.memberRoom.MemberRoomUpdateRequestDto;
import com.example.nettalk.entity.room.Room;
import com.example.nettalk.service.RoomService;
import com.example.nettalk.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class ApiController {
    private final RoomService roomService;
    private final UserService userService;

    @GetMapping("/mypage")
    public ResponseEntity Mypage() {
        return userService.mypage(SecurityUtil.getCurrentUserId());
    }

    @GetMapping("/rooms")
    public ResponseEntity LoadList() {
        return roomService.list();
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity LoadList(@PathVariable("id") Long id) {
        return roomService.getRoom(SecurityUtil.getCurrentUserId(), id);
    }

    @PostMapping("/rooms/{id}/name")
    public void updateUserRoom(@PathVariable("id") Long id, @RequestBody MemberRoomUpdateRequestDto memberRoomUpdateRequestDto) {
        roomService.updateUserRoom(id, memberRoomUpdateRequestDto);
    }

    @PostMapping("/rooms")
    public ResponseEntity create(@RequestBody Room room) {
        return roomService.createRoom(SecurityUtil.getCurrentUserId(), room);
    }

}
