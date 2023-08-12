package com.miniproject.kubeBoard.service

import com.miniproject.kubeBoard.client.DeploymentClient
import com.miniproject.kubeBoard.entity.deployment.res.DeploymentListResponse
import com.miniproject.kubeBoard.repository.deployment.DeploymentQuerydslRepository
import com.miniproject.kubeBoard.repository.deployment.DeploymentRepository
import io.fabric8.kubernetes.api.model.apps.Deployment
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeploymentService (
        private val deploymentRepository: DeploymentRepository,
        private val deploymentClient: DeploymentClient,
        private val deploymentQuerydslRepository: DeploymentQuerydslRepository,
){
    @Transactional
    fun syncDeploymentList(){
        //기존 DeploymentList 전체 삭제
        deploymentRepository.deleteAll()
        //동기화해온 리스트 저장
        val deploymentList= deploymentClient.getDeploymentList()
        deploymentRepository.saveAll(deploymentList)
    }

    fun getDeploymentList(offset: Int, sublist: Int): DeploymentListResponse {
        val count = deploymentRepository.findAll().size
        val deploymentList = deploymentQuerydslRepository.getDeploymentList(offset, sublist)
        return DeploymentListResponse(count,deploymentList)
    }

    fun getDeployment(name: String): DeploymentListResponse {
        val deployment = deploymentQuerydslRepository.getDeployment(name)
        return DeploymentListResponse(1, listOf(deployment))
    }

    fun getDeploymentListAll(): DeploymentListResponse {
        val deploymentList = deploymentRepository.findAll()
        return DeploymentListResponse(deploymentList.size,deploymentList);
    }

    fun getDeploymentClientList(): MutableList<Deployment>? {
        return deploymentClient.getDeploymentClientList()
    }


}