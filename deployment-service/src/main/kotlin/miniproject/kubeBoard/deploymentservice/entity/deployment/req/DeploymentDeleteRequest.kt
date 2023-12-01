package miniproject.kubeBoard.podservice.entity.pod.req

data class DeploymentDeleteRequest(
        val namespace: String,
        val name: String,
        val userId: String
)