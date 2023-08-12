package com.miniproject.kubeBoard.entity.deployment.res

import com.miniproject.kubeBoard.entity.deployment.DeploymentData

data class DeploymentListResponse (
        val count: Int,
        val list: List<DeploymentData?>
)