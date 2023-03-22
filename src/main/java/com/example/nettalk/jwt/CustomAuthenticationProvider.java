package com.example.nettalk.jwt;

import com.example.nettalk.service.AuthService;
import com.example.nettalk.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private String message;
    @Override
    public Authentication authenticate(Authentication authentication) {
        try {
            if(authentication == null){
                message = "Authentication is null";
                throw new InternalAuthenticationServiceException(message);
            }
            String username = authentication.getName();
            if(authentication.getCredentials() == null){
                message = "Credentials is null";
                throw new AuthenticationCredentialsNotFoundException(message);
            }
            String password = authentication.getCredentials().toString();
            UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);
            if(loadedUser == null){
                message = "UserDetailsService returned null, which is an interface contract violation";
                throw new InternalAuthenticationServiceException(message);
            }
            /* checker */
            if(!loadedUser.isAccountNonLocked()){
                message = "User account is locked";
                throw new LockedException(message);
            }
            if(!loadedUser.isEnabled()){
                message = "User is disabled";
                throw new DisabledException(message);
            }
            if(!loadedUser.isAccountNonExpired()){
                message = "User account has expired";
                throw new AccountExpiredException(message);
            }
            /* 실질적인 인증 */
            if(!passwordEncoder.matches(password, loadedUser.getPassword())){
                message = "Password does not match stored value";
                throw new BadCredentialsException(message);
            }
            /* checker */
            if(!loadedUser.isCredentialsNonExpired()){
                message = "User credentials have expired";
                throw new CredentialsExpiredException(message);
            }
            /* 인증 완료 */
            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
            result.setDetails(authentication.getDetails());
            return result;
        } catch (Exception e) {
            if(!message.equals("")) {
                AuthService.data.put("message", message);
            }
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}