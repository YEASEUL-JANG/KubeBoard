package miniproject.kubeBoard.serviceservice.repository.service

import miniproject.kubeBoard.serviceservice.entity.service.ServiceData
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceRepository:JpaRepository<ServiceData,Long> {
    fun save(serviceData: ServiceData)
}