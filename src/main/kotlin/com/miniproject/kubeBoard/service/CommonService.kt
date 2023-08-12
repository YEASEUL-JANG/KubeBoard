package com.miniproject.kubeBoard.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

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
    }
}