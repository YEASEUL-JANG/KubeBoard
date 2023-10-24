package miniproject.kubeBoard.logservice.service

import miniproject.kubeBoard.logservice.entity.log.LogData
import miniproject.kubeBoard.logservice.entity.log.req.UserLogRequest
import miniproject.kubeBoard.logservice.entity.log.res.UserLogResponse
import miniproject.kubeBoard.logservice.repository.log.LogRepository
import org.springframework.stereotype.Service
import javax.persistence.EntityManager

@Service
class LogService (
    private val logRepository: LogRepository,
    private val entityManager: EntityManager,
){

    fun getUserLog(userId: String): UserLogResponse {
        val logDataList = logRepository.findAllByUserId(userId)

        return UserLogResponse(logDataList.size, logDataList)
    }

    fun saveUserLog(userLogRequest: UserLogRequest): UserLogResponse {
        val logData = LogData.of(userLogRequest);
        logRepository.save(logData);
        return UserLogResponse(1,listOf(logRepository.findByUserId(userLogRequest.userId)));
    }

}