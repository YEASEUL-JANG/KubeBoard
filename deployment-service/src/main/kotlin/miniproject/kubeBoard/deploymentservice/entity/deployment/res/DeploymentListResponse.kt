package miniproject.kubeBoard.deploymentservice.entity.deployment.res

import miniproject.kubeBoard.deploymentservice.entity.deployment.DeploymentData

data class DeploymentListResponse (
        val count: Int,
        val list: List<DeploymentData?>
)