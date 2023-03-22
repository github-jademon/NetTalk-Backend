package com.example.nettalk.service;

import com.example.nettalk.entity.member.Member;
import com.example.nettalk.entity.member.MemberRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    private UserDetails createUserDetails(Member member) {
        try {
            System.out.println(member.getEmail());
            System.out.println(member.getPassword());

            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

            System.out.println("0000"+grantedAuthority);

            User user = new User(
                    String.valueOf(member.getId()),
                    member.getPassword(),
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
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(()->new UsernameNotFoundException("Username not found '"+ username + "'"));
    }
}
