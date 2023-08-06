package com.miniproject.kubeBoard.entity.pod.res

import com.miniproject.kubeBoard.entity.pod.PodData

data class PodListResponse(
        val count: Int,
        val podList: List<PodData>
)