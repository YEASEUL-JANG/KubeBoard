package com.example.userservice.controller;

import com.example.userservice.dto.RequestUser;
import com.example.userservice.dto.ResponseUser;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.dto.UserDto;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/user-service")
public class UserController {

    private Environment env;
    private UserService userService;

    @Autowired
    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
    }
    @GetMapping("/welcome")
    @Timed(value = "users.welcome",longTask = true)
    public String welcome(){
        return env.getProperty("greeting.message");
       // return greeting.getMessage();
    }
    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    public String status(){
        return String.format("It's working in User Service "
                + ", port(local.server.port)= "+ env.getProperty("local.server.port")
                + ", port(server.port)= "+ env.getProperty("server.port")
                + ", with token secret= "+ env.getProperty("token.secret")
                + ", with token time=" + env.getProperty("token.expiration_time"));
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user,UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();
        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v,ResponseUser.class));
        });
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/users/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserDto userDto = userService.getUserByUserId(userId);
        ResponseUser responseUser = new ModelMapper().map(userDto,ResponseUser.class);
        return ResponseEntity.ok(responseUser);
    }

}
