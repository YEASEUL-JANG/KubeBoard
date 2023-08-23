package com.miniproject.kubeBoard.service

import com.miniproject.kubeBoard.client.ServiceClient
import com.miniproject.kubeBoard.entity.service.ServiceListResponse
import com.miniproject.kubeBoard.repository.service.ServiceQuerydslRepository
import com.miniproject.kubeBoard.repository.service.ServiceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ServiceService (
        private val serviceRepository: ServiceRepository,
        private val serviceClient: ServiceClient,
        private val serviceQuerydslRepository: ServiceQuerydslRepository,
){
    @Transactional
    fun syncServiceList(){
        //기존 Service 전체 삭제
        serviceRepository.deleteAll()
        //동기화해온 리스트 저장
        val serviceList= serviceClient.getServiceList()
        serviceRepository.saveAll(serviceList)
    }

    fun getServiceList(offset: Int, sublist: Int): ServiceListResponse {
        val count = serviceRepository.findAll().size
        val serviceList = serviceQuerydslRepository.getServiceList(offset, sublist)
        return ServiceListResponse(count,serviceList)
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

    fun getSearchServiceList(name: String, offset: Int, sublist: Int): ServiceListResponse {
        val serviceList = serviceQuerydslRepository.getSearchServiceList(name,offset, sublist)
        val count = serviceQuerydslRepository.getSearchServiceList(name,null,null).size
        return ServiceListResponse(count,serviceList)
    }


}