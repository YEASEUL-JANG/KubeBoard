package miniproject.kubeBoard.serviceservice.entity.service.req

data class ServiceDeleteRequest (
    val name: String,
    val namespace: String,
    val userId: String
)
