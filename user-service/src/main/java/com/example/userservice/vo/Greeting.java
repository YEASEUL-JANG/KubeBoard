package com.example.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
//@AllArgsConstructor : argument를 모두 가지고 있는 생성자를 만듦
public class Greeting {
    @Value("${greeting.message}")
    private String message;

}
