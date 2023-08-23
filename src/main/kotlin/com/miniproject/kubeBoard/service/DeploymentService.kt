package com.miniproject.kubeBoard.service

import com.miniproject.kubeBoard.client.DeploymentClient
import com.miniproject.kubeBoard.entity.deployment.res.DeploymentListResponse
import com.miniproject.kubeBoard.repository.deployment.DeploymentQuerydslRepository
import com.miniproject.kubeBoard.repository.deployment.DeploymentRepository
import io.fabric8.kubernetes.api.model.apps.Deployment
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
class DeploymentService (
        private val deploymentRepository: DeploymentRepository,
        private val deploymentClient: DeploymentClient,
        private val deploymentQuerydslRepository: DeploymentQuerydslRepository,
        private val entityManager: EntityManager,
){
    @Transactional
    fun syncDeploymentList(){
        //기존 List 일괄 삭제
        deploymentQuerydslRepository.deleteAll()
        //동기화해온 리스트 일괄 저장
        val deploymentList= deploymentClient.getDeploymentList()
        for(deployment in deploymentList){
            entityManager.persist(deployment)
        }
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

    fun changeReplica(name: String, namespace: String, scale: Int) {
        deploymentClient.changeReplica(name, namespace, scale);
    }

    fun getSearchDeployment(search: String?, offset: Int, sublist: Int): DeploymentListResponse {
        val deploymentList = deploymentQuerydslRepository.getSearchDeploymentList(search,offset,sublist);
        val count = deploymentQuerydslRepository.getSearchDeploymentList(search,null,null).size;
        return DeploymentListResponse(count,deploymentList)
    }


}