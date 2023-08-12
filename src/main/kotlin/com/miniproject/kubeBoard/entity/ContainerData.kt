package com.miniproject.kubeBoard.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.miniproject.kubeBoard.entity.pod.PodData
import io.fabric8.kubernetes.api.model.Container
import io.fabric8.kubernetes.api.model.ContainerStatus
import javax.persistence.*

@Entity(name = "container")
class ContainerData(
        val containerName: String,
        val containerImage: String,
        val ready: Boolean,
        val started: Boolean,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "pod_idx")
        @JsonBackReference
        val podData: PodData,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val containerIdx: Long?=null,

) {
    companion object{
        fun of(container: Container, status: ContainerStatus, podData: PodData):ContainerData{
            return ContainerData(
                    containerName = container.name,
                    containerImage = container.image,
                    ready = status.ready,
                    started = status.started,
                    podData = podData,
            )
        }
    }

}