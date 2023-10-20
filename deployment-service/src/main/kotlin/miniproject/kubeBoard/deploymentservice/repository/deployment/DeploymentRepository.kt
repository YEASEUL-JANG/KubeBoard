package miniproject.kubeBoard.deploymentservice.repository.deployment

import miniproject.kubeBoard.deploymentservice.entity.deployment.DeploymentData
import org.springframework.data.jpa.repository.JpaRepository

interface DeploymentRepository:JpaRepository<DeploymentData,Long> {
    fun save(deploymentData: DeploymentData)
}