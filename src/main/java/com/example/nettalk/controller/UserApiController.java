package com.example.nettalk.controller;

import com.example.nettalk.vo.DuplicateVo;
import com.example.nettalk.entity.UserEntity;
import com.example.nettalk.response.DefaultRes;
import com.example.nettalk.response.ResponseMessage;
import com.example.nettalk.response.StatusCode;
import com.example.nettalk.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;

@RestController
@RequestMapping("/api/user")
public class UserApiController {
    @Autowired
    private UserService userService;

    // signin
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody UserEntity user){
        try {
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            // todo : sign in
            System.out.println(user);

            return new ResponseEntity(DefaultRes
                    .res(StatusCode.OK, ResponseMessage.OK), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // signup
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserEntity user){
        try {
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            // todo : requestbody user data test
            PasswordEncoder passwordEncoder = userService.getPasswordEncoder();

            UserEntity userEntity = UserEntity.builder()
                    .email(user.getEmail())
                    .userid(user.getUserid())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .build();

            userService.insert(userEntity);

            return new ResponseEntity(DefaultRes
                    .res(StatusCode.OK, ResponseMessage.OK), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/exists")
    public ResponseEntity checkUserDataDuplicate(@RequestBody DuplicateVo user) {
        try {
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            HashMap<String, Object> hashMap = new HashMap<String, Object>();

            String type = user.getType();
            String data = user.getData();

            if(type.equals("userid")) {
                hashMap.put("check", userService.checkUseridDuplicate(data));
            } else if (type.equals("email")) {
                hashMap.put("check", userService.checkEmailDuplicate(data));
            } else {
                return new ResponseEntity(DefaultRes
                        .res(StatusCode.METHOD_NOT_ALLOWED, ResponseMessage.METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
            }

            return new ResponseEntity(DefaultRes
                    .res(StatusCode.OK, ResponseMessage.OK, hashMap), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
