package com.example.nettalk.repository;

import com.example.nettalk.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByUserid(String userid);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Long id);
}
