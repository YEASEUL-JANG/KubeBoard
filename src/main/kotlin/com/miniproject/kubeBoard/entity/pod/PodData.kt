package com.miniproject.kubeBoard.entity.pod

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.miniproject.kubeBoard.entity.ContainerData
import io.fabric8.kubernetes.api.model.Pod
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity(name = "pod")
class PodData (
        val podName: String,
        val namespace: String,
        val phase: String,
        val podIp: String,
        val createdTime: String,
        val nodeName: String,
        val labels: String,

        //detailData
        @OneToMany(mappedBy = "podData", cascade = [CascadeType.ALL], orphanRemoval = true )
        @JsonManagedReference
        var containerList: MutableList<ContainerData> = mutableListOf(),

        val serviceAccountName: String,
        val uid: String,
        val qosClass: String,
        val ownerName: String,
        val ownerUid: String,
        val ownerKind: String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val podIdx: Long?= null,
){
        companion object{
                fun of(pod: Pod): PodData {
                        val podData = PodData(
                                podName = pod.metadata.name,
                                namespace = pod.metadata.namespace,
                                phase = pod.status.phase,
                                podIp = pod.status.podIP,
                                createdTime = pod.metadata.creationTimestamp,
                                nodeName = pod.spec.nodeName,
                                labels = getLabel(pod.metadata.labels),
                                serviceAccountName = pod.spec.serviceAccountName,
                                uid=pod.metadata.uid,
                                qosClass = pod.status.qosClass,
                                ownerName = pod.metadata.ownerReferences.get(0).name,
                                ownerUid = pod.metadata.ownerReferences.get(0).uid,
                                ownerKind = pod.metadata.ownerReferences.get(0).kind,

                        )
                        podData.containerList.addAll(getContainerList(pod, podData))
                        return podData
                }

                private fun getContainerList(pod: Pod, podData: PodData): List<ContainerData> {
                        val containerList = mutableListOf<ContainerData>()
                        for( container in pod.spec.containers){
                                for( status in pod.status.containerStatuses){
                                        if(status.name.equals(container.name)){
                                                containerList.add(ContainerData.of(container,status, podData))
                                                break
                                        }
                                }
                        }
                        return containerList
                }

                private fun getLabel(labels: Map<String, String>): String {
                        val objectMapper = ObjectMapper()
                        try{
                               return objectMapper.writeValueAsString(labels)
                        }catch (e:JsonProcessingException){
                               e.printStackTrace()
                        }
                        return ""
                }
        }
}
