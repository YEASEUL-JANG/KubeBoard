package miniproject.kubeBoard.serviceservice.entity.service


data class ServiceListResponse (
        val count: Int,
        val list: List<ServiceData?>
)