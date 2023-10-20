package miniproject.kubeBoard.serviceservice.client

import miniproject.kubeBoard.serviceservice.entity.service.ServiceData
import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.client.KubernetesClient

@org.springframework.stereotype.Service
class ServiceClient(
        private val client: KubernetesClient
) {
    fun getServiceList():List<ServiceData>{
        val serviceList = mutableListOf<ServiceData>()
        val services = client.services().list().items
        if(services != null){
            for(service in services){
                serviceList.add(ServiceData.of(service))
            }
        }
        return serviceList
    }
    fun getServiceClientList(): MutableList<Service>? {
        return client.services().list().items
    }
}