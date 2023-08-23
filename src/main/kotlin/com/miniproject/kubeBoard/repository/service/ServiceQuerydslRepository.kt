package com.miniproject.kubeBoard.repository.service

import com.miniproject.kubeBoard.entity.deployment.QDeploymentData
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

    fun getSearchServiceList(name: String, offset: Int?, sublist: Int?): List<ServiceData> {
        val searchCondition = serviceData.serviceName.containsIgnoreCase(name)
                .or(serviceData.namespace.containsIgnoreCase(name))
                .or(serviceData.labels.containsIgnoreCase(name))

        val query =  queryFactory.select(serviceData)
                .from(serviceData)
                .where(searchCondition)
        offset?.let {query.offset(it.toLong()) }
        sublist?.let {query.limit(it.toLong()) }
        return query.fetch()
    }

}