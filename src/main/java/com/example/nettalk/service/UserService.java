package com.example.nettalk.service;
import com.example.nettalk.dto.UserResponseDto;
import com.example.nettalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public UserResponseDto findUserInfoById(Long userId) {
        return userRepository.findById(userId)
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    public UserResponseDto findUserInfoByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
    public boolean checkUseridDuplicate(String userid) {
        return userRepository.existsByUserid(userid);
    }

}
