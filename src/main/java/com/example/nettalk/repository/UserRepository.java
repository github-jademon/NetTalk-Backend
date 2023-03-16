package com.example.nettalk.repository;

import com.example.nettalk.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    boolean existsByEmail(String email);
    boolean existsByUserid(String userid);
    Optional<UserEntity> findByEmail(String email);
}
