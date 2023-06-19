package com.example.nettalk.controller;

import com.example.nettalk.dto.chat.ChatMessageDto;
import com.example.nettalk.jwt.JwtProperties;
import com.example.nettalk.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class StompChatController implements ChannelInterceptor {
    private static final Map<String, Integer> sessions = new HashMap<>();
    private final SimpMessagingTemplate template;
    private final ChatMessageService chatMessageService;

    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDto message){
        message.setMessage(message.getUsername() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        chatMessageService.save(message.getRoomId(), message.toChatMessage(), "system");
    }

    @MessageMapping(value = "/chat/exit")
    public void exit(ChatMessageDto message){
        message.setMessage(message.getUsername() + "님이 퇴장하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        chatMessageService.save(message.getRoomId(), message.toChatMessage(), "system");
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message){
        System.out.println("message: "+ message.getRoomId());
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        chatMessageService.save(message.getRoomId(), message.toChatMessage(), "user");
        System.out.println("sessions: ");
        Set<String> s = sessions.keySet();
        for(String i:s) {
            System.out.println(sessions.get(i)+" "+i);
        }
    }

    @EventListener(SessionConnectEvent.class)
    public void onConnect(SessionConnectEvent event){
        try {
            MessageHeaders messageHeaders = event.getMessage().getHeaders();
            System.out.println(messageHeaders);
            String sessionId = messageHeaders.get("simpSessionId").toString();
            if(messageHeaders.get("nativeHeaders").toString().contains(JwtProperties.BEARER_PREFIX)) {
                String userId = messageHeaders.get("nativeHeaders").toString().split("User=\\[")[1].split("]")[0];

                sessions.put(sessionId, Integer.valueOf(userId));

//                System.out.println(sessions.toString());
            } else {
                sessions.put(sessionId, 0);
            }
            System.out.println("??????????????????????????????????????????"+sessionId);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @EventListener(SessionDisconnectEvent.class)
    public void onDisconnect(SessionDisconnectEvent event) {
        sessions.remove(event.getSessionId());
    }
}