package miniproject.kubeBoard.serviceservice.entity.service.req

data class ServiceCreateRequest (
    val name: String,
    val namespace: String,
    val port: Int,
    val targetPort: Int,
    val protocol: String,
    val label: String,
    val type: String,
    val userId: String
)
