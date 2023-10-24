package miniproject.kubeBoard.logservice.entity.log

import miniproject.kubeBoard.logservice.entity.log.req.UserLogRequest
import java.util.*
import javax.persistence.*

@Entity(name = "userlog")
class LogData(
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val requestMs: String,
    @Column(nullable = false)
    val requestData: String,
    @Column(nullable = false)
    val requestSource: String,
    @Column(nullable = false)
    val requestNum: Int,
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    val requestedTime: Date? = null,

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val userlogIdx: Long? = null,
) {
    companion object {
        fun of(request: UserLogRequest): LogData {
            return LogData(
                    userId = request.userId,
                requestMs = request.requestMs,
                requestData = request.requestData,
                requestSource = request.requestSource,
                requestNum = request.requestNum
            )
        }
    }
}
