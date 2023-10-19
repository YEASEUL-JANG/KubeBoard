package miniproject.kubeBoard.podservice.entity.pod.res

import miniproject.kubeBoard.podservice.entity.pod.PodData

data class PodListResponse(
        val count: Int,
        val list: List<PodData?>
)