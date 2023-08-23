package com.miniproject.kubeBoard.repository.deployment

import com.miniproject.kubeBoard.entity.deployment.DeploymentData
import com.miniproject.kubeBoard.entity.deployment.QDeploymentData
import com.miniproject.kubeBoard.entity.deployment.QDeploymentData.deploymentData
import com.miniproject.kubeBoard.entity.pod.QPodData
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class DeploymentQuerydslRepository (
        private val queryFactory: JPAQueryFactory
){
    fun getDeploymentList(offset: Int, sublist: Int):List<DeploymentData> {
        return queryFactory.select(deploymentData)
                .from(deploymentData)
                .offset(offset.toLong())
                .limit(sublist.toLong())
                .fetch()
    }

    fun getDeployment(name: String): DeploymentData? {
        return queryFactory.select(deploymentData)
                .from(deploymentData)
                .where(deploymentData.deploymentName.eq(name))
                .fetchOne()
    }

    fun getSearchDeploymentList(name: String, offset: Int?, sublist: Int?): List<DeploymentData> {
        val searchCondition = deploymentData.deploymentName.containsIgnoreCase(name)
                .or(deploymentData.namespace.containsIgnoreCase(name))
                .or(deploymentData.labels.containsIgnoreCase(name))

        val query =  queryFactory.select(deploymentData)
                .from(deploymentData)
                .where(searchCondition)
        offset?.let {query.offset(it.toLong()) }
        sublist?.let {query.limit(it.toLong()) }
        return query.fetch()
    }
}