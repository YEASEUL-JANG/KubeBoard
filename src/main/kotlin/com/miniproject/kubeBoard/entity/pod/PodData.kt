package com.miniproject.kubeBoard.entity.pod

import io.fabric8.kubernetes.api.model.Pod
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "pod")
class PodData (
        private val podName: String,
        private val namespace: String,
        private val phase: String,
        private val podIp: String,
        private val createdTime: String,
        private val nodeName: String,
        private val labels: String,
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private val podIdx: Long?= null,
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
