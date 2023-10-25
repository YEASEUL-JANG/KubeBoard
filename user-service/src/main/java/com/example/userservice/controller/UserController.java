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

    /**
     *  회원가입
     * @param user
     * @return
     */

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user,UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
    /**
     *  회원목록
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();
        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v,ResponseUser.class));
        });
        return ResponseEntity.ok(result);
    }
    /**
     *  상세 회원목록
     * @return
     */
    @GetMapping(value = "/users/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
        UserDto userDto = userService.getUserByUserId(userId);
        ResponseUser responseUser = new ModelMapper().map(userDto,ResponseUser.class);
        return ResponseEntity.ok(responseUser);
    }
    /**
     *  회원가입
     * @param userId
     * @return
     */

    @PostMapping("/idCheck")
    public ResponseEntity idCheck(@RequestBody String userId){
        int userCount = userService.duplicateUser(userId);
        boolean isValid = false;
        if(userCount==0){
            isValid = true;
        }
        return ResponseEntity.status(HttpStatus.OK).body(isValid);
    }

}
