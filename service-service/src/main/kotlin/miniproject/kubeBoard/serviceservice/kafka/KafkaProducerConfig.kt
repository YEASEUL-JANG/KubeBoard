package miniproject.kubeBoard.deploymentservice.kafka

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Configuration
class KafkaProducerConfig (
    @Value("\${kafka.server}") private val kafkaServer : String
    ){
    @Bean
    fun producerFactory() : ProducerFactory<String,String> {
        val configProps = hashMapOf<String,Any>(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaServer,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String,String> {
        return KafkaTemplate(producerFactory())
    }
}
