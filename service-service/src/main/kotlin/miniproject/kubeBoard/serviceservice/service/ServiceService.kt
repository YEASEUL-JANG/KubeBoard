package miniproject.kubeBoard.serviceservice.service

import miniproject.kubeBoard.serviceservice.client.ServiceClient
import miniproject.kubeBoard.serviceservice.entity.service.ServiceListResponse
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


}