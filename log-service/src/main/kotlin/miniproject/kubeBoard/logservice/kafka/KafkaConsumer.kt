package miniproject.kubeBoard.logservice.kafka

import aj.org.objectweb.asm.TypeReference
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import miniproject.kubeBoard.logservice.entity.log.LogData
import miniproject.kubeBoard.logservice.entity.log.req.UserLogRequest
import miniproject.kubeBoard.logservice.repository.log.LogRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

// ObjectMapper에 대한 확장 함수를 최상위 수준에서 선언
inline fun <reified T> ObjectMapper.readValueFromJson(json: String): T = this.readValue(json)

@Service
class KafkaConsumer @Autowired constructor(
    private val logRepository: LogRepository
) {
    companion object{
        private val log = LoggerFactory.getLogger(KafkaConsumer::class.java)
    }
    @KafkaListener(topics = ["log-service"])
    fun processMessage(userLogRequest: String){
        log.info("Kafka message  ====> $userLogRequest")


        try {
            val mapper = jacksonObjectMapper()
            val map: Map<Any, Any> = mapper.readValueFromJson(userLogRequest)

            val userLogRequest = UserLogRequest(
                userId = map["userId"] as? String ?: "",
                requestData = map["requestData"] as? String ?: "",
                requestMs = map["requestMs"] as? String ?: "",
                requestSource = map["requestSource"] as? String ?: "",
                requestNum = (map["requestNum"] as? Number)?.toInt() ?: 0
            )
            val logData = LogData.of(userLogRequest);
            logRepository.save(logData)
        }catch (e: JsonProcessingException){
            e.printStackTrace()
        } catch (e: Exception) {
            // 기타 예외 처리
            log.error("Exception in processing message: ${e.message}")
            // 필요한 경우 메시지 처리를 중단하거나 재시도 등의 조치를 취함
        }
    }
}

