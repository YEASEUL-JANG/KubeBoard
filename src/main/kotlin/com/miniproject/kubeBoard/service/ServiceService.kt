package com.miniproject.kubeBoard.service

import com.miniproject.kubeBoard.client.ServiceClient
import com.miniproject.kubeBoard.entity.service.ServiceListResponse
import com.miniproject.kubeBoard.repository.service.ServiceQuerydslRepository
import com.miniproject.kubeBoard.repository.service.ServiceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
class ServiceService (
        private val serviceRepository: ServiceRepository,
        private val serviceClient: ServiceClient,
        private val serviceQuerydslRepository: ServiceQuerydslRepository,
        private val entityManager: EntityManager,
){
    @Transactional
    fun syncServiceList(){
        //기존 podList 일괄 삭제
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