package miniproject.kubeBoard.podservice.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import miniproject.kubeBoard.podservice.entity.pod.req.UserLogRequest
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducer
    (private val kafkaTemplate: KafkaTemplate<String,String>){
    companion object {
        private val log = LoggerFactory.getLogger(KafkaProducer::class.java)
    }
    fun send(kafkaTopic: String, userLogRequest: UserLogRequest): UserLogRequest {
        val mapper = jacksonObjectMapper()
        val jsonInString: String
        try {
            jsonInString = mapper.writeValueAsString(userLogRequest)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        kafkaTemplate.send(kafkaTopic, jsonInString)
        log.info("Kafka Producer send data from the deployment-service: $userLogRequest")
        return userLogRequest
    }
    }
