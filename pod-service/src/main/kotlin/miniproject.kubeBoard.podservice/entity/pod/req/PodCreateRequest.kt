package miniproject.kubeBoard.podservice.entity.pod.req

import miniproject.kubeBoard.podservice.entity.pod.PodData

data class PodCreateRequest(
        val namespace: String,
        val podName: String,
)