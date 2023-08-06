package com.miniproject.kubeBoard.entity.pod

import io.fabric8.kubernetes.api.model.Pod
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "pod")
class PodData (
        val podName: String,
        val namespace: String,
        val phase: String,
        val podIp: String,
        val createdTime: String,
        val nodeName: String,
        val labels: String,
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val podIdx: Long?= null,
){
        companion object{
                fun of(pod: Pod): PodData {
                        return PodData(
                                podName = pod.metadata.name,
                                namespace = pod.metadata.namespace,
                                phase = pod.status.phase,
                                podIp = pod.status.podIP,
                                createdTime = pod.metadata.creationTimestamp,
                                nodeName = pod.spec.nodeName,
                                labels = pod.metadata.labels.toString(),
                        )
                }
        }
}
