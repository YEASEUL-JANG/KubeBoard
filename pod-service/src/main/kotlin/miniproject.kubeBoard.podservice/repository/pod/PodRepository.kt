package miniproject.kubeBoard.podservice.repository.pod

import miniproject.kubeBoard.podservice.entity.pod.PodData
import org.springframework.data.jpa.repository.JpaRepository

interface PodRepository:JpaRepository<PodData,Long> {
    fun save(podData: PodData)
}