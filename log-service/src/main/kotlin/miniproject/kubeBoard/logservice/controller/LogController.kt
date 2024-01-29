package miniproject.kubeBoard.logservice.controller

import miniproject.kubeBoard.logservice.dto.ResponseUserLog
import miniproject.kubeBoard.logservice.entity.log.LogData
import miniproject.kubeBoard.logservice.entity.log.req.UserLogRequest
import miniproject.kubeBoard.logservice.entity.log.res.UserLogResponse
import miniproject.kubeBoard.logservice.service.LogService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/log-service")
class LogController (
        private val logService: LogService
){
    /**
     * User Log 생성
     */
    @PostMapping("/save/{userId}")
    fun saveLog(
        @PathVariable("userId") userLogRequest: UserLogRequest,
    ): UserLogResponse {
        return logService.saveUserLog(userLogRequest);
    }

    /**
     * User Log 상세 데이터
     */
    @GetMapping("/list/{userId}")
    fun getLogList(
            @PathVariable("userId") userId:String,
            @RequestParam(value = "page", required = false, defaultValue = "1") page: Int
    ): List<ResponseUserLog> {
        val offset= (page-1)*5
        val sublist=5*page
        return logService.getUserLog(userId,offset,sublist);
    }
}