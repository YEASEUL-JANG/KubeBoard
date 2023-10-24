package com.example.userservice.dto;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class RequestUser {
    @NotNull( message="Email cannot be null" )
    @Size(min=2, message = "Email not be less than two characters")
    @Email
    private String email;
    @NotNull( message="Password cannot be null" )
    @Size(min=8, message = "Password must be equal or grater than 8 characters")
    private String pwd;
    @NotNull( message="Id cannot be null" )
    @Size(min=2, message = "Id not be less than 2 characters")
    private String userId;

}
