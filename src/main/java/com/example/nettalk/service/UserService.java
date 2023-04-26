package com.example.nettalk.service;

import com.example.nettalk.dto.member.MemberResponseDto;
import com.example.nettalk.entity.chat.ChatMessage;
import com.example.nettalk.entity.member.Member;
import com.example.nettalk.entity.member.MemberRepository;
import com.example.nettalk.entity.room.Room;
import com.example.nettalk.entity.room.RoomRepository;
import com.example.nettalk.entity.user_room.UserRoom;
import com.example.nettalk.entity.user_room.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final MemberRepository memberRepository;

    public MemberResponseDto findMemberInfoById(Long userId) {
        return memberRepository.findById(userId)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    public MemberResponseDto findMemberInfoByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    public MemberResponseDto findMemberInfoByUserid(String userid) {
        return memberRepository.findByUserid(userid)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    public Member getMember(Long userId) {
        return memberRepository.findById(userId).get();
    }

}
