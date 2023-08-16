package com.miniproject.kubeBoard.repository.service

import com.miniproject.kubeBoard.entity.service.ServiceData
import org.springframework.data.jpa.repository.JpaRepository

interface ServiceRepository:JpaRepository<ServiceData,Long> {
    fun save(serviceData: ServiceData)
}