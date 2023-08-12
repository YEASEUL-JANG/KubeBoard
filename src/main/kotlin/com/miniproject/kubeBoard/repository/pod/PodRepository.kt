package com.miniproject.kubeBoard.repository.pod

import com.miniproject.kubeBoard.entity.pod.PodData
import org.springframework.data.jpa.repository.JpaRepository

interface PodRepository:JpaRepository<PodData,Long> {
    fun save(podData: PodData)
}