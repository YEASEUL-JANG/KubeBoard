package miniproject.kubeBoard.podservice.entity.pod.req

import lombok.Builder
import lombok.Data
import lombok.Getter
import lombok.Setter

@Getter
@Setter
@Builder
data class UserLogRequest(
        val userId: String,
        val requestMs: String,
        val requestData: String,
        val requestSource: String,
        val requestNum: Int,
)