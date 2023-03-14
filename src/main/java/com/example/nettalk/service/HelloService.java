package com.example.nettalk.service;

import com.example.nettalk.entity.HelloEntity;
import com.example.nettalk.repository.HelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    @Autowired
    private HelloRepository helloRepository;

    public HelloEntity insert(HelloEntity helloEntity) {
        return helloRepository.save(helloEntity);
    }
}
