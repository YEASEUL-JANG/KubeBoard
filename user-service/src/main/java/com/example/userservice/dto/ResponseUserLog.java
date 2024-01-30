package com.example.userservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //json 데이터에서 null 값은 제외시킴
public class ResponseUserLog {
    private List<ResponseUserLogData> userLogDataList;
    private int logCount;

    public ResponseUserLog(List<ResponseUserLogData> userLogDataList, int logCount) {
        this.userLogDataList = userLogDataList;
        this.logCount = logCount;
    }
}
