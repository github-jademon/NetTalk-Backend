package com.example.nettalk.service;

import com.example.nettalk.entity.UserEntity;
import com.example.nettalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository =userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public UserEntity insert(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

//    public User read(String email) {
//        Optional<UserEntity> optional = userRepository.findByEmail(email);
//        if (optional.isPresent()) {
//            UserEntity entity = optional.get();
//            return new User(entity.getEmail(), entity.getPassword(), null);
//        } else {
//            throw new IllegalArgumentException();
//        }

//    }
    public boolean checkUseridDuplicate(String userid) {
        return userRepository.existsByUserid(userid);
    }

}
