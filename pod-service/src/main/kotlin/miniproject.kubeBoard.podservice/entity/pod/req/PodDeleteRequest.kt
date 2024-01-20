package miniproject.kubeBoard.podservice.entity.pod.req

import miniproject.kubeBoard.podservice.entity.pod.PodData

data class PodDeleteRequest(
        val namespace: String,
        val name: String,
        val userId: String
)