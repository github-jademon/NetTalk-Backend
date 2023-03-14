package com.example.nettalk.controller;

import com.example.nettalk.entity.HelloEntity;
import com.example.nettalk.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
    @Autowired
    private HelloService helloService;

    @PostMapping("/hello")
    public HelloEntity signup(@RequestBody HelloEntity req) {
        System.out.println("1");
        System.out.println(req.getName());

        HelloEntity helloEntity = HelloEntity.builder()
                .name(req.getName())
                .age(req.getAge())
                .build();

        return helloService.insert(helloEntity);
    }

    @GetMapping("/hello")
    public ModelAndView hello() {
        ModelAndView mv = new ModelAndView("hello");

        return mv;
    }
}
