package miniproject.kubeBoard.serviceservice.entity.service

data class ServiceCreateRequest (
    val name: String,
    val namespace: String,
    val port: Int,
    val targetPort: Int,
    val protocol: String
)
