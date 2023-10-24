package com.example.userservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long useridx;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false, unique = true)
    private String encrytedPwd;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;
}
