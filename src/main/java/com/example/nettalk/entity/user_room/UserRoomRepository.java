package com.example.nettalk.entity.user_room;

import com.example.nettalk.entity.member.Member;
import com.example.nettalk.entity.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
    Optional<UserRoom> findByMemberAndRoom(Member member, Room room);
    List<UserRoom> findAllByMember(Member member);
}
