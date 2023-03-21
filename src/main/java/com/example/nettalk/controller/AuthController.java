package com.example.nettalk.controller;

import com.example.nettalk.dto.TokenDto;
import com.example.nettalk.dto.TokenRequestDto;
import com.example.nettalk.dto.UserRequestDto;
import com.example.nettalk.dto.UserResponseDto;
import com.example.nettalk.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(authService.signup(userRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(authService.login(userRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    // signup
//    @PostMapping("/signup")
//    public ResponseEntity signup(@RequestBody UserEntity user){
//        try {
//            HttpHeaders headers= new HttpHeaders();
//            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//            // todo : requestbody user data test
//            PasswordEncoder passwordEncoder = userService.getPasswordEncoder();
//
//            UserEntity userEntity = UserEntity.builder()
//                    .email(user.getEmail())
//                    .userid(user.getUserid())
//                    .password(passwordEncoder.encode(user.getPassword()))
//                    .build();
//
//            userService.insert(userEntity);
//
//            return new ResponseEntity(DefaultRes
//                    .res(StatusCode.OK, ResponseMessage.OK), HttpStatus.OK);
//        } catch(Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    @GetMapping("/exists")
//    public ResponseEntity checkUserDataDuplicate(@RequestBody DuplicateVo user) {
//        try {
//            HttpHeaders headers= new HttpHeaders();
//            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//            HashMap<String, Object> hashMap = new HashMap<String, Object>();
//
//            String type = user.getType();
//            String data = user.getData();
//
//            if(type.equals("userid")) {
//                hashMap.put("check", userService.checkUseridDuplicate(data));
//            } else if (type.equals("email")) {
//                hashMap.put("check", userService.checkEmailDuplicate(data));
//            } else {
//                return new ResponseEntity(DefaultRes
//                        .res(StatusCode.METHOD_NOT_ALLOWED, ResponseMessage.METHOD_NOT_ALLOWED), HttpStatus.METHOD_NOT_ALLOWED);
//            }
//
//            return new ResponseEntity(DefaultRes
//                    .res(StatusCode.OK, ResponseMessage.OK, hashMap), HttpStatus.OK);
//        } catch(Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
