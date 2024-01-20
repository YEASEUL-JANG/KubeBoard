package miniproject.kubeBoard.serviceservice.client

import miniproject.kubeBoard.serviceservice.entity.service.ServiceData
import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.api.model.ServiceBuilder
import io.fabric8.kubernetes.client.KubernetesClient
import miniproject.kubeBoard.serviceservice.entity.service.req.ServiceCreateRequest
import miniproject.kubeBoard.serviceservice.entity.service.req.ServiceDeleteRequest

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
            .addToLabels(splitKeyValue(result)?.first,splitKeyValue(result)?.second)
            .endMetadata()
            .withNewSpec()
            .withType(serviceCreateRequest.type)
            .addNewPort()
            .withProtocol(serviceCreateRequest.protocol)
            .withPort(serviceCreateRequest.port)
            .withNewTargetPort(serviceCreateRequest.targetPort)
            .endPort()
            .addToSelector(splitKeyValue(result)?.first,splitKeyValue(result)?.second)
            .endSpec()
            .build()

        client.services().inNamespace(serviceCreateRequest.namespace).create(service)
    }
    fun splitKeyValue(input:String):Pair<String,String>?{
        val parts = input.split("=")
        if(parts.size == 2){
            return Pair(parts[0],parts[1])
        }
        return null
    }

    fun deleteService(serviceDeleteRequest: ServiceDeleteRequest) {
        client.services().inNamespace(serviceDeleteRequest.namespace).withName(serviceDeleteRequest.name).delete()
    }
}