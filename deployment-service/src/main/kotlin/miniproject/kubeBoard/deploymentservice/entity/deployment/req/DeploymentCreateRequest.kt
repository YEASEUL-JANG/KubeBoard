package miniproject.kubeBoard.podservice.entity.pod.req

data class DeploymentCreateRequest(
        val namespace: String,
        val name: String,
)