package com.example.userservice.service;


import com.example.userservice.client.LogServiceClient;
import com.example.userservice.dto.ResponseUserLog;
import com.example.userservice.dto.UserLogRequest;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.kafka.KafkaProducer;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private RestTemplate restTemplate;
    private Environment env;
    private CircuitBreakerFactory circuitBreakerFactory;
    private LogServiceClient logServiceClient;
    private KafkaProducer kafkaProducer;
    @Autowired
    //생성자 주입으로 주입된 객체들은 빈으로 등록이 되어있어야 한다.
    //BCryptPasswordEncoder 은 빈으로 개발자가 등록한적이 없기때문에 그냥 주입하면 오류 -> 가장먼저 실행되는 스프링 앱의 기동클래스에 빈으로 주입시킨다.
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           Environment env,
                           RestTemplate restTemplate,
                           LogServiceClient logServiceClient,
                           CircuitBreakerFactory circuitBreakerFactory,
                           KafkaProducer kafkaProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.restTemplate = restTemplate;
        this.logServiceClient = logServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);
        UserDto returnUserDto = mapper.map(userEntity,UserDto.class);
        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId, int page) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null){
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        /* CircuitBreaker*/
        log.info("Before call log microservice");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        ResponseUserLog logList = circuitBreaker.run(() -> logServiceClient.getLogList(userId, page),
                throwable -> new ResponseUserLog(new ArrayList<>(),0));
        log.info("logList : "+ logList.toString());
        log.info("After called log microservice");
        userDto.setLogs(logList);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null)
            throw new UsernameNotFoundException(userId);
        return new ModelMapper().map(userEntity,UserDto.class);
    }

    @Override
    public int duplicateUser(String userId) {
       return (int) userRepository.countByUserId(userId);
    }

    @Override
    public void logout(String userId) {
        //Kafka 로그아웃 로그
        UserLogRequest userLogRequest = UserLogRequest.builder()
                .requestData("logout 로그아웃")
                .userId(userId)
                .requestMs("user-service")
                .requestSource("logout")
                .build();
        kafkaProducer.send("log-service",userLogRequest);
    }

    @Override
    //전달받은 아이디를 가지고 유저를 찾아오는 메서드
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null)
            throw new UsernameNotFoundException(userId);
        return  new User(userEntity.getUserId(), userEntity.getEncryptedPwd(),
                true, true, true, true, new ArrayList<>());
    }


}
