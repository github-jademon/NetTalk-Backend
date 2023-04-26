package com.example.nettalk.hanlder;

import com.example.nettalk.config.SecurityUtil;
import com.example.nettalk.jwt.JwtProperties;
import com.example.nettalk.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
public class stompInterceptor implements ChannelInterceptor {
    private final TokenProvider tokenProvider;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        System.out.println("full message:" + message);
        if (headerAccessor!=null&&StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            System.out.println("msg: " + "conne");
            String token = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
            token = token.replace(JwtProperties.BEARER_PREFIX, " ");
            System.out.println("auth:" + token);

            try {
                if(StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    Long userid = SecurityUtil.getCurrentUserId();

                    if(userid == null) {
                        throw new Exception("userid가 null입니다.");
                    }

                    headerAccessor.addNativeHeader("User", String.valueOf(userid));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return message;
    }
}