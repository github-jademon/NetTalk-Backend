package com.example.nettalk.service;

import com.example.nettalk.entity.UserEntity;
import com.example.nettalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public String encode(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserEntity insert(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
