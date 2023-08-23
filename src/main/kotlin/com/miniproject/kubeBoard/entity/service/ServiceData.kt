package com.miniproject.kubeBoard.entity.service

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.miniproject.kubeBoard.service.CommonService
import io.fabric8.kubernetes.api.model.Service
import javax.persistence.*

@Entity(name="service")
class ServiceData (
        val serviceName: String,
        val labels: String,
        val createdTime: String,
        val clusterIp: String,
        val type: String,
        val namespace: String,
        val uid: String,
        val sessionAffinity: String,
        val selector: String,

        @OneToMany(mappedBy = "serviceData", cascade = [CascadeType.ALL], orphanRemoval = true)
        @JsonManagedReference
        val ports: MutableList<PortData> = mutableListOf(),

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val serviceIdx: Long? = null,
){
        companion object {
                fun of(service: Service): ServiceData {
                val serviceData = ServiceData(
                        serviceName = service.metadata.name,
                        labels = CommonService.getLabel(service.metadata.labels),
                        createdTime = service.metadata.creationTimestamp,
                        clusterIp = service.spec.clusterIP,
                        type = service.spec.type,
                        namespace = service.metadata.namespace,
                        uid = service.metadata.uid,
                        sessionAffinity = service.spec.sessionAffinity,
                        selector = CommonService.getLabel(service.spec.selector),
                )
                        serviceData.ports.addAll(getPorts(service,serviceData))
                        return serviceData;
                }

                private fun getPorts(service: Service, serviceData: ServiceData): Collection<PortData> {
                        val portList = mutableListOf<PortData>()
                        for(port in service.spec.ports){
                                portList.add(PortData.of(port,serviceData))
                        }
                        return portList
                }
        }
}