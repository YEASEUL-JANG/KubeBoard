package com.example.userservice.error;

import lombok.Builder;
import lombok.Data;

@Data
public class ErrorResponse {
    private String errorMessage;
    @Builder
    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
