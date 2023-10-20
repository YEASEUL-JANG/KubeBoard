package miniproject.kubeBoard.serviceservice.entity.service

import com.fasterxml.jackson.annotation.JsonBackReference
import io.fabric8.kubernetes.api.model.*
import javax.persistence.*

@Entity(name = "ports")
class PortData(
        val nodePort: String?,
        val port: String,
        val protocol: String,
        val targetPort: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "service_idx")
        @JsonBackReference
        val serviceData: ServiceData,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val portIdx: Long?=null,

        ) {
    companion object{
        fun of(port: ServicePort, serviceData: ServiceData): PortData {
            return PortData(
                    nodePort = port.nodePort.toString(),
                    port = port.port.toString(),
                    protocol = port.protocol,
                    targetPort = port.targetPort.value.toString(),
                    serviceData = serviceData,
            )
        }
    }

}