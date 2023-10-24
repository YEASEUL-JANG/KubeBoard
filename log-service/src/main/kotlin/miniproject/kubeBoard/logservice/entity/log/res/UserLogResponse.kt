package miniproject.kubeBoard.logservice.entity.log.res

import miniproject.kubeBoard.logservice.entity.log.LogData

data class UserLogResponse(
        val count: Int,
        val list: List<LogData?>
)