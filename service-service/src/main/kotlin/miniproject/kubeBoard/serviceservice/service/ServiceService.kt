package miniproject.kubeBoard.serviceservice.service

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import miniproject.kubeBoard.serviceservice.client.ServiceClient
import miniproject.kubeBoard.serviceservice.entity.service.req.ServiceCreateRequest
import miniproject.kubeBoard.serviceservice.entity.service.req.ServiceDeleteRequest
import miniproject.kubeBoard.serviceservice.entity.service.ServiceListResponse
import miniproject.kubeBoard.serviceservice.entity.service.req.UserLogRequest
import miniproject.kubeBoard.serviceservice.kafka.KafkaProducer
import miniproject.kubeBoard.serviceservice.repository.service.PortQuerydslRepository
import miniproject.kubeBoard.serviceservice.repository.service.ServiceQuerydslRepository
import miniproject.kubeBoard.serviceservice.repository.service.ServiceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
class ServiceService (
        private val serviceRepository: ServiceRepository,
        private val serviceClient: ServiceClient,
        private val serviceQuerydslRepository: ServiceQuerydslRepository,
        private val portQuerydslRepository: PortQuerydslRepository,
        private val entityManager: EntityManager,
        private val kafkaProducer: KafkaProducer

){
    @Transactional
    fun syncServiceList(){
        //기존 serviceList 일괄 삭제
        portQuerydslRepository.deleteAll()
        serviceQuerydslRepository.deleteAll()
        //동기화해온 리스트 일괄 저장
        val serviceList= serviceClient.getServiceList()
        for(service in serviceList){
            entityManager.persist(service)
        }
    }
    fun getService(name: String): ServiceListResponse {
        val service = serviceQuerydslRepository.getService(name)
        return ServiceListResponse(1, listOf(service))
    }

    fun getServiceListAll(): ServiceListResponse {
        val serviceList = serviceRepository.findAll()
        return ServiceListResponse(serviceList.size,serviceList);
    }

    fun getServiceClientList(): MutableList<io.fabric8.kubernetes.api.model.Service>? {
        return serviceClient.getServiceClientList()
    }

    fun getSearchServiceList(search: String?, offset: Int, sublist: Int): ServiceListResponse {
        val serviceList = serviceQuerydslRepository.getSearchServiceList(search,offset, sublist)
        val count = serviceQuerydslRepository.getSearchServiceList(search,null,null).size
        return ServiceListResponse(count,serviceList)
    }
    fun getServiceStatus(namespace: String, name: String): Boolean {
        return serviceClient.getServiceStatus(namespace, name);
    }
    @Transactional
    fun createService(serviceCreateRequest: ServiceCreateRequest): String {
        serviceClient.createService(serviceCreateRequest)
        val maxAttempts = 10
        var currentAttempt = 0
        runBlocking {
            while (currentAttempt < maxAttempts){
                val status = getServiceStatus(serviceCreateRequest.namespace, serviceCreateRequest.name)
                if(status) {
                    syncServiceList()
                    break
                }
            }
            delay(2000)
            currentAttempt++
        }
        //로그 저장
        savelogData(
                serviceCreateRequest.name,
                1,
                "service",
                "create",
                serviceCreateRequest.userId
        )
        return serviceCreateRequest.name
    }
    @Transactional
    fun deleteService(serviceDeleteRequest: ServiceDeleteRequest): Boolean {
        serviceClient.deleteService(serviceDeleteRequest)
        val maxAttempts = 10
        var currentAttempt = 0
        var isDeploymentDeleted = false
        runBlocking {
            while (currentAttempt < maxAttempts){
                val status = getServiceStatus(
                    serviceDeleteRequest.namespace,
                    serviceDeleteRequest.name,
                )
                if(!status){
                    isDeploymentDeleted = true
                    syncServiceList()
                    break
                }
                delay(2000)
                currentAttempt++
            }
        }
        //로그 저장
        savelogData(
                serviceDeleteRequest.name,
                1,
                "service",
                "delete",
                serviceDeleteRequest.userId
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