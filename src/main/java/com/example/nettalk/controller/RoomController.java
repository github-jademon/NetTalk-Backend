package com.example.nettalk.controller;

import com.example.nettalk.config.SecurityUtil;
import com.example.nettalk.dto.memberRoom.MemberRoomUpdateRequestDto;
import com.example.nettalk.dto.room.RoomRequestDto;
import com.example.nettalk.service.RoomService;
import com.example.nettalk.service.UserRoomService;
import com.example.nettalk.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class RoomController {
    private final RoomService roomService;
    private final UserService userService;
    private final UserRoomService userRoomService;

    @GetMapping("/mypage")
    public ResponseEntity mypage() {
        return userRoomService.mypage(userService.getMember(SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/rooms")
    public ResponseEntity loadList() {
        return roomService.list();
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity load(@PathVariable("id") Long id) {
        return userRoomService.getRoom(roomService.getRoom(id), userService.getMember(SecurityUtil.getCurrentUserId()));
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        return userRoomService.deleteRoom(roomService.getRoom(id), userService.getMember(SecurityUtil.getCurrentUserId()));
    }

    @PostMapping("/rooms/{id}/name")
    public void updateUserRoom(@PathVariable("id") Long id, @RequestBody MemberRoomUpdateRequestDto memberRoomUpdateRequestDto) {
        userRoomService.updateUserRoom(roomService.getRoom(id), userService.getEmailMember(memberRoomUpdateRequestDto.getEmail()), memberRoomUpdateRequestDto.getName());
    }

    @PostMapping("/rooms")
    public ResponseEntity create(@RequestBody RoomRequestDto room) {
        return userRoomService.createRoom(userService.getMember(SecurityUtil.getCurrentUserId()), room.getRoom(), room.getUsername());
    }

}
