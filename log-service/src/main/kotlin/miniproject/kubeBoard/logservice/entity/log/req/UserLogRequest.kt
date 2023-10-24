package miniproject.kubeBoard.logservice.entity.log.req

import lombok.Data
@Data
data class UserLogRequest(
        val userId: String,
        val requestMs: String,
        val requestData: String,
        val requestSource: String,
        val requestNum: Int,
)