package miniproject.kubeBoard.logservice.dto;

data class ResponseUserLog(
        val logCount: Int,
        val userLogDataList: List<ResponseUserLogData?>
)
