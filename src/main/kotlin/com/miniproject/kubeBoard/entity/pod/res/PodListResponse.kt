package com.miniproject.kubeBoard.entity.pod.res

import com.miniproject.kubeBoard.entity.pod.PodData

data class PodListResponse(
        val count: Int,
        val list: List<PodData?>
)