package com.miniproject.kubeBoard.repository.deployment

import com.miniproject.kubeBoard.entity.deployment.DeploymentData
import com.miniproject.kubeBoard.entity.pod.PodData
import org.springframework.data.jpa.repository.JpaRepository

interface DeploymentRepository:JpaRepository<DeploymentData,Long> {
    fun save(deploymentData: DeploymentData)
}