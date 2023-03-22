package com.example.nettalk.controller;

import com.example.nettalk.dto.member.MemberResponseDto;
import com.example.nettalk.config.SecurityUtil;
import com.example.nettalk.service.UserService;
import com.example.nettalk.vo.duplicate.DuplicateVo;
import com.example.nettalk.vo.response.DefaultRes;
import com.example.nettalk.vo.response.ResponseMessage;
import com.example.nettalk.vo.response.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> findMemberInfoById() {
        return ResponseEntity.ok(userService.findMemberInfoById(SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/exists")
    public ResponseEntity checkMemberDataDuplicate(@RequestBody DuplicateVo user) {

        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        String type = user.getType();
        String data = user.getData();

        if(type.equals("userid")) {
            hashMap.put("check", userService.findMemberInfoByUserid(data));
        } else if (type.equals("email")) {
            hashMap.put("check", userService.findMemberInfoByEmail(data));
        } else {
            return new ResponseEntity(DefaultRes
                    .res(StatusCode.METHOD_NOT_ALLOWED, ResponseMessage.METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
        }

        return new ResponseEntity(DefaultRes
                .res(StatusCode.OK, ResponseMessage.OK, hashMap), HttpStatus.OK);
    }

}
