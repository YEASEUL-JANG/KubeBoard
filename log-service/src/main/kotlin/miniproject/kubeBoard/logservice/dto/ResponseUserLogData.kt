package miniproject.kubeBoard.logservice.dto;

import miniproject.kubeBoard.logservice.entity.log.LogData
import miniproject.kubeBoard.logservice.service.CommonService

data class ResponseUserLogData(
        val userId: String,
        val requestData: String,
        val requestMs: String,
        val requestSource: String,
        val requestNum: Int,
        val requestedTime: String
){
    companion object {
        fun of(request: LogData): ResponseUserLogData {
            return ResponseUserLogData(
                    userId = request.userId,
                    requestMs = request.requestMs,
                    requestData = request.requestData,
                    requestSource = request.requestSource,
                    requestNum = request.requestNum,
                    requestedTime = CommonService.translateForm(request.requestedTime.toString()),
            )
        }
    }
}
