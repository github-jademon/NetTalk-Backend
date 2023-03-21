package com.example.nettalk.service;

import com.example.nettalk.entity.UserEntity;
import com.example.nettalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    private UserDetails createUserDetails(UserEntity userEntity) {
        try {
            System.out.println(userEntity.getEmail());
            System.out.println(userEntity.getPassword());

            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userEntity.getAuthority().toString());

            System.out.println("0000"+grantedAuthority);

            User user = new User(
                    String.valueOf(userEntity.getId()),
                    userEntity.getPassword(),
                    Collections.singleton(grantedAuthority)
            );

            System.out.println("1111"+user);
            System.out.println("2222"+user.getPassword());

            return user;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("!!!!!!!!!!"+username);
        return userRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(()->new UsernameNotFoundException("Username not found '"+ username + "'"));
    }
}
