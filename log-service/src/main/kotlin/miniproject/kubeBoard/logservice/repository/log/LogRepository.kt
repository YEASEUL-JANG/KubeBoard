package miniproject.kubeBoard.logservice.repository.log

import miniproject.kubeBoard.logservice.entity.log.LogData
import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository:JpaRepository<LogData,Long> {
    fun save(logData: LogData)
    fun findAllByUserId(userId: String): List<LogData>
    fun findByUserId(userId: String): LogData
}