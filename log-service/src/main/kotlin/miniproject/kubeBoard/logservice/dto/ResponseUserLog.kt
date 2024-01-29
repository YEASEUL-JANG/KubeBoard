package miniproject.kubeBoard.logservice.dto;

import miniproject.kubeBoard.logservice.entity.log.LogData
import miniproject.kubeBoard.logservice.entity.log.req.UserLogRequest
import java.util.Date

data class ResponseUserLog (
    val userId: String,
    val requestData: String,
    val requestMs: String,
    val requestSource: String,
    val requestNum: Int,
    val requestedTime: Date?
){
    companion object {
        fun of(request: LogData): ResponseUserLog {
            return ResponseUserLog(
                    userId = request.userId,
                    requestMs = request.requestMs,
                    requestData = request.requestData,
                    requestSource = request.requestSource,
                    requestNum = request.requestNum,
                    requestedTime = request.requestedTime,
            )
        }
    }
}
