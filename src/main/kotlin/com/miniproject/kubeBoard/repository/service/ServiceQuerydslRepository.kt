package com.miniproject.kubeBoard.repository.service

import com.miniproject.kubeBoard.entity.pod.PodData
import com.miniproject.kubeBoard.entity.pod.QPodData.podData
import com.miniproject.kubeBoard.entity.service.QServiceData.serviceData
import com.miniproject.kubeBoard.entity.service.ServiceData
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class ServiceQuerydslRepository (
        private val queryFactory: JPAQueryFactory
){
    fun getServiceList(offset: Int, sublist: Int):List<ServiceData> {
        return queryFactory.select(serviceData)
                .from(serviceData)
                .offset(offset.toLong())
                .limit(sublist.toLong())
                .fetch()
    }

    fun getService(name: String): ServiceData? {
        return queryFactory.select(serviceData)
                .from(serviceData)
                .where(serviceData.serviceName.eq(name))
                .fetchOne()
    }
}