package miniproject.kubeBoard.serviceservice.client

import miniproject.kubeBoard.serviceservice.entity.service.ServiceData
import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.api.model.ServiceBuilder
import io.fabric8.kubernetes.client.KubernetesClient
import miniproject.kubeBoard.serviceservice.entity.service.ServiceCreateRequest

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

    fun getServiceStatus(namespace: String, serviceName: String): Boolean {
        val service = client.services().inNamespace(namespace).withName(serviceName).get() ?: return false
        val endpoints = client.endpoints().inNamespace(namespace).withName(serviceName).get() ?: return false

        return endpoints.subsets.any { subset ->
            subset.addresses.isNotEmpty() && subset.ports.isNotEmpty()
        }
    }

    fun createService(serviceCreateRequest: ServiceCreateRequest) {
        val result = serviceCreateRequest.label
        val service = ServiceBuilder()
            .withNewMetadata()
            .withName(serviceCreateRequest.name)
            .endMetadata()
            .withNewSpec()
            .withType("ClusterIP")
            .addNewPort()
            .withProtocol(serviceCreateRequest.protocol)
            .withPort(serviceCreateRequest.port)
            .withNewTargetPort(serviceCreateRequest.targetPort)
            .endPort()
            .addToSelector(splitKeyValue(result)?.first,splitKeyValue(result)?.second)
            .endSpec()
            .build()

        client.services().inNamespace("default").create(service)
    }
    fun splitKeyValue(input:String):Pair<String,String>?{
        val parts = input.split(":")
        if(parts.size == 2){
            return Pair(parts[0],parts[1])
        }
        return null
    }
}