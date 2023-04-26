package com.example.nettalk.controller;

import com.example.nettalk.dto.member.MemberResponseDto;
import com.example.nettalk.config.SecurityUtil;
import com.example.nettalk.service.AuthService;
import com.example.nettalk.service.UserService;
import com.example.nettalk.vo.duplicate.DuplicateVo;
import com.example.nettalk.response.DefaultRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> findMemberInfoById() {
        try {
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            DefaultRes ress = DefaultRes.res(HttpStatus.OK, HttpStatus.OK.value(), authService.res.getResponseMessage(), userService.findMemberInfoById(SecurityUtil.getCurrentUserId()));

            return new ResponseEntity(ress, ress.getHttpStatus());
        } catch(Exception e) {
            e.printStackTrace();
            DefaultRes ress = DefaultRes.res(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), authService.res.getResponseMessage());

            return new ResponseEntity(ress, ress.getHttpStatus());
        }
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
            DefaultRes ress = DefaultRes.res(HttpStatus.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.name());

            return new ResponseEntity(ress, ress.getHttpStatus());
        }
        DefaultRes ress = DefaultRes.res(HttpStatus.OK, HttpStatus.OK.value(), HttpStatus.OK.name(), hashMap);

        return new ResponseEntity(ress, ress.getHttpStatus());
    }

}
