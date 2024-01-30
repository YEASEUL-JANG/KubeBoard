package miniproject.kubeBoard.logservice.service

import miniproject.kubeBoard.logservice.dto.ResponseUserLog
import miniproject.kubeBoard.logservice.dto.ResponseUserLogData
import miniproject.kubeBoard.logservice.entity.log.LogData
import miniproject.kubeBoard.logservice.entity.log.req.UserLogRequest
import miniproject.kubeBoard.logservice.entity.log.res.UserLogResponse
import miniproject.kubeBoard.logservice.repository.log.LogQuerydslRepository
import miniproject.kubeBoard.logservice.repository.log.LogRepository
import org.springframework.stereotype.Service
import javax.persistence.EntityManager

@Service
class LogService (
    private val logRepository: LogRepository,
    private val logQuerydslRepository: LogQuerydslRepository,
    private val entityManager: EntityManager,
){

    fun getUserLog(userId: String, offset: Int, sublist: Int): List<ResponseUserLogData> {
        val result : List<LogData> = logQuerydslRepository.findAllByUserId(userId,offset,sublist)
        return  result.map { logData -> ResponseUserLogData.of(logData) }
    }

    fun saveUserLog(userLogRequest: UserLogRequest): UserLogResponse {
        val logData = LogData.of(userLogRequest);
        logRepository.save(logData);
        return UserLogResponse(1,listOf(logRepository.findByUserId(userLogRequest.userId)));
    }

    fun getLogCount(userId: String): Int {
        val logList : List<LogData> =  logRepository.findAllByUserId(userId);
        return logList.size
    }

}