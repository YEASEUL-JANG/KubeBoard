package com.example.userservice.dto;

import lombok.Data;

@Data
    public class ResponseUserLogData {
        private String userId;
        private String requestData;
        private String requestMs;
        private String requestSource;
        private Integer requestNum;
        private String requestedTime;
}
