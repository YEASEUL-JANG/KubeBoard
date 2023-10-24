package com.example.userservice.dto;

import lombok.Data;

import java.util.Date;
@Data
    public class ResponseUserLog {
        private String userId;
        private String requestData;
        private String requestMs;
        private String requestSource;
        private Integer requestNum;
        private Date requestAt;
}
