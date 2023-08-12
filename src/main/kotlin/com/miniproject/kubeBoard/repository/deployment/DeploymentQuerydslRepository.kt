package com.miniproject.kubeBoard.repository.deployment

import com.miniproject.kubeBoard.entity.deployment.DeploymentData
import com.miniproject.kubeBoard.entity.deployment.QDeploymentData.deploymentData
import com.miniproject.kubeBoard.entity.pod.PodData
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
}