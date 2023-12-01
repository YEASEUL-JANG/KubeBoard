package miniproject.kubeBoard.deploymentservice.service

import miniproject.kubeBoard.deploymentservice.client.DeploymentClient
import miniproject.kubeBoard.deploymentservice.entity.deployment.res.DeploymentListResponse
import miniproject.kubeBoard.deploymentservice.repository.deployment.DeploymentQuerydslRepository
import miniproject.kubeBoard.deploymentservice.repository.deployment.DeploymentRepository
import io.fabric8.kubernetes.api.model.apps.Deployment
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import miniproject.kubeBoard.deploymentservice.entity.deployment.req.UserLogRequest
import miniproject.kubeBoard.deploymentservice.kafka.KafkaProducer
import miniproject.kubeBoard.deploymentservice.repository.deployment.ConditionQuerydslRepository
import miniproject.kubeBoard.podservice.entity.pod.req.DeploymentCreateRequest
import miniproject.kubeBoard.podservice.entity.pod.req.DeploymentDeleteRequest
import miniproject.kubeBoard.podservice.entity.pod.req.DeploymentScaleRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
class DeploymentService (
        private val deploymentRepository: DeploymentRepository,
        private val deploymentClient: DeploymentClient,
        private val deploymentQuerydslRepository: DeploymentQuerydslRepository,
        private val conditionQuerydslRepository: ConditionQuerydslRepository,
        private val entityManager: EntityManager,
    private val kafkaProducer: KafkaProducer
){
    @Transactional
    fun syncDeploymentList(){
        //기존 List 일괄 삭제
        conditionQuerydslRepository.deleteAll()
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

    fun changeReplica(deploymentScaleRequest : DeploymentScaleRequest) {
        deploymentClient.changeReplica(deploymentScaleRequest);
    }

    fun getSearchDeployment(search: String?, offset: Int, sublist: Int): DeploymentListResponse {
        val deploymentList = deploymentQuerydslRepository.getSearchDeploymentList(search,offset,sublist);
        val count = deploymentQuerydslRepository.getSearchDeploymentList(search,null,null).size;
        return DeploymentListResponse(count,deploymentList)
    }
    fun getDeploymentStatus(namespace: String, name: String): Boolean {
        return deploymentClient.getDeploymentStatus(namespace, name);
    }
    @Transactional
    fun createDeployment(deploymentCreateRequest: DeploymentCreateRequest): String {
        deploymentClient.createDeployment(deploymentCreateRequest)
        val maxAttempts = 10
        var currentAttempt = 0
        runBlocking {
            while (currentAttempt < maxAttempts){
                val status = getDeploymentStatus(deploymentCreateRequest.namespace, deploymentCreateRequest.name)
                if(status) {
                    syncDeploymentList()
                    break
                }
            }
            delay(2000)
            currentAttempt++
        }
        //로그 저장
        savelogData(
            deploymentCreateRequest.name,
            1,
            "deployment",
            "create",
            deploymentCreateRequest.userId
        )
        return deploymentCreateRequest.name
    }
    @Transactional
    fun deleteDeployment(deploymentDeleteRequest: DeploymentDeleteRequest): Boolean {
        deploymentClient.deleteDeployment(deploymentDeleteRequest)
        val maxAttempts = 10
        var currentAttempt = 0
        var isDeploymentDeleted = false
        runBlocking {
            while (currentAttempt < maxAttempts){
                val status = getDeploymentStatus(
                    deploymentDeleteRequest.namespace,
                    deploymentDeleteRequest.name,
                )
                if(!status){
                    isDeploymentDeleted = true
                    syncDeploymentList()
                    break
                }
                delay(2000)
                currentAttempt++
            }
        }
        //로그 저장
        savelogData(
            deploymentDeleteRequest.name,
            1,
            "deployment",
            "delete",
            deploymentDeleteRequest.userId
        )
        return isDeploymentDeleted;
    }

    fun savelogData(name: String, scale: Int, requestData: String,
                    requestSource: String, userId: String) {
        val userLogRequest = UserLogRequest(
            userId = userId,
            requestMs = "deployment-service",
            requestData = requestData,
            requestSource = requestSource,
            requestNum = scale
        )
        kafkaProducer.send("log-service",userLogRequest)
    }


}