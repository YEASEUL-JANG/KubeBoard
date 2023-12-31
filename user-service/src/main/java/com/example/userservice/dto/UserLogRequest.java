package com.example.userservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class UserLogRequest {
    private String userId;
    private String requestData;
    private String requestMs;
    private String requestSource;
    private Integer requestNum;

    @Builder
    private UserLogRequest(String userId, String requestData, String requestMs, String requestSource, Integer requestNum) {
        this.userId = userId;
        this.requestData = requestData;
        this.requestMs = requestMs;
        this.requestSource = requestSource;
        this.requestNum = requestNum;
    }
}
