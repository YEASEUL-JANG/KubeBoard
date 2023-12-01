package miniproject.kubeBoard.podservice.entity.pod.req

data class DeploymentScaleRequest(
        val namespace: String,
        val name: String,
        val scale: Int,
        val userId: String
)