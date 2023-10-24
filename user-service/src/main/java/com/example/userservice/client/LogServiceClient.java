package com.example.userservice.client;

import com.example.userservice.dto.ResponseUserLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
@FeignClient(name = "log-service") //호출하고자 하는 microService 이름
public interface LogServiceClient {

    @GetMapping("/log-service/list/{userId}")
    List<ResponseUserLog> getLog(@PathVariable
                                  String userId);
}
