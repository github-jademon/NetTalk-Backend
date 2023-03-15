package com.example.nettalk.controller;

import com.example.nettalk.entity.UserEntity;
import com.example.nettalk.response.DefaultRes;
import com.example.nettalk.response.ResponseMessage;
import com.example.nettalk.response.StatusCode;
import com.example.nettalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;

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

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody UserEntity userEntity){
        try {
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            //로그인

            return new ResponseEntity(DefaultRes
                    .res(StatusCode.OK, ResponseMessage.OK), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserEntity userEntity){
        try {
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            //회원가입

            return new ResponseEntity(DefaultRes
                    .res(StatusCode.OK, ResponseMessage.OK), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user-email/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkEmailDuplicate(email));
    }

    @GetMapping("/user-id/{userid}/exists")
    public ResponseEntity<Boolean> checkUseridDuplicate(@PathVariable String userid) {
        return ResponseEntity.ok(userService.checkUseridDuplicate(userid));
    }

}
