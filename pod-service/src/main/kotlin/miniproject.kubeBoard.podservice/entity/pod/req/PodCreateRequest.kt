package miniproject.kubeBoard.podservice.entity.pod.req

data class PodCreateRequest(
        val namespace: String,
        val name: String,
        val userId: String
)