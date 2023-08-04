package com.miniproject.kubeBoard.client

import com.miniproject.kubeBoard.entity.PodData
import io.fabric8.kubernetes.client.KubernetesClient
import org.springframework.stereotype.Service

@Service
class PodClient(
        private val client: KubernetesClient
) {
    fun getPodList():List<PodData>{
        val podList = mutableListOf<PodData>()
        val pods = client.pods()?.list()?.items
        if(pods != null){
            for(pod in pods){
                podList.add(PodData.of(pod))
            }
        }
        return podList
    }
}