package com.miniproject.kubeBoard.client

import com.miniproject.kubeBoard.entity.pod.PodData
import io.fabric8.kubernetes.api.model.Pod
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
    fun getPodClientList(): MutableList<Pod>? {
        return client.pods()?.list()?.items
    }
}