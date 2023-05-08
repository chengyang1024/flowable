package com.chengy;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Fenpei {


    @Bean
    public String getUser(){
        return "王东";
    }

}
