package miniproject.kubeBoard.deploymentservice.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class CommonService (){
    companion object {
        fun getLabel(labels: Map<String, String>): String {
            val objectMapper = ObjectMapper()
            try{
                return objectMapper.writeValueAsString(labels)
            }catch (e: JsonProcessingException){
                e.printStackTrace()
            }
            return ""
        }

        fun translateForm(time: String): String {
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val date = LocalDateTime.parse(time,dateTimeFormatter).atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("Asia/Seoul"))
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        }
    }
}