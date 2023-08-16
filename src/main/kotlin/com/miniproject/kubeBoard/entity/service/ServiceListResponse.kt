package com.miniproject.kubeBoard.entity.service


data class ServiceListResponse (
        val count: Int,
        val list: List<ServiceData?>
)