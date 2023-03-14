package com.example.nettalk.repository;

import com.example.nettalk.entity.HelloEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloRepository extends CrudRepository<HelloEntity, Integer> {
}
