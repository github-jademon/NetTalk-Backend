package com.example.nettalk.controller;

import com.example.nettalk.dto.token.TokenDto;
import com.example.nettalk.dto.token.TokenRequestDto;
import com.example.nettalk.dto.member.MemberRequestDto;
import com.example.nettalk.vo.response.DefaultRes;
import com.example.nettalk.vo.response.StatusCode;
import com.example.nettalk.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody MemberRequestDto memberRequestDto, BindingResult errors) {
        try {
            if(errors.hasErrors()) {
                Map<String, String> validatorResult = new HashMap<>();

                for (FieldError error : errors.getFieldErrors()) {
                    String validKeyName = String.format("valid_%s", error.getField());
                    validatorResult.put(validKeyName, error.getDefaultMessage());
                }

                return new ResponseEntity(validatorResult, HttpStatus.OK);
            } else {
                HttpHeaders headers= new HttpHeaders();
                headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

                authService.data.put("data", authService.signup(memberRequestDto));

                if(authService.data.get("data")==null) {
                    throw new Exception();
                }

                return new ResponseEntity(DefaultRes
                        .res(authService.res.getStatusCode(), authService.res.getResponseMessage(), authService.data), HttpStatus.OK);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes
                    .res(authService.res.getStatusCode(), authService.res.getResponseMessage()), HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberRequestDto memberRequestDto) {
        try {
            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            authService.data.put("token", authService.login(memberRequestDto));

            return new ResponseEntity(DefaultRes
                    .res(StatusCode.OK, authService.res.getResponseMessage(), authService.data), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(DefaultRes
                    .res(authService.res.getStatusCode(), authService.res.getResponseMessage()), HttpStatus.OK);
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

}
