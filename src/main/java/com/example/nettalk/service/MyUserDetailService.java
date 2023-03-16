package com.example.nettalk.service;

import com.example.nettalk.domain.MyUserDetails;
import com.example.nettalk.entity.UserEntity;
import com.example.nettalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserService userService;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(email == null || email.equals("")) {
            throw new UsernameNotFoundException(email);
        }
        try {
            Optional<UserEntity> user = userRepository.findByEmail(email);
            log.info("User Found in the Database: {}", email);
            return new MyUserDetails(user.get());
        } catch (IllegalArgumentException e) {
            log.info("User Not Found in the Database");
            throw new UsernameNotFoundException(email);
        }
    }
}
