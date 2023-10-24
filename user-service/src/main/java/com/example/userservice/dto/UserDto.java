package com.example.userservice.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String encryptedPwd;

    private List<ResponseUserLog> logs;
}
