package com.miniproject.kubeBoard.service

import com.miniproject.kubeBoard.client.PodClient
import com.miniproject.kubeBoard.entity.pod.res.PodListResponse
import com.miniproject.kubeBoard.repository.PodQuerydslRepository
//import com.miniproject.kubeBoard.repository.PodQuerydslRepository
import com.miniproject.kubeBoard.repository.PodRepository
import io.fabric8.kubernetes.api.model.Pod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PodService (
        private val podRepository: PodRepository,
        private val podClient: PodClient,
        private val podQuerydslRepository: PodQuerydslRepository,
){
    @Transactional
    fun syncPodList(){
        //기존 podList 전체 삭제
        podRepository.deleteAll()
        //동기화해온 리스트 저장
        val podList= podClient.getPodList()
        podRepository.saveAll(podList)
    }

    fun getPodList(offset: Int, sublist: Int): PodListResponse {
        val count = podRepository.findAll().size
        val podList = podQuerydslRepository.getPodList(offset, sublist)
        return PodListResponse(count,podList)
    }

    fun getPod(name: String): PodListResponse {
        val pod = podQuerydslRepository.getPod(name)
        return PodListResponse(1, listOf(pod))
    }

    fun getPodListAll(): PodListResponse {
        val podList = podRepository.findAll()
        return PodListResponse(podList.size,podList);
    }

    fun getPodClientList(): MutableList<Pod>? {
        return podClient.getPodClientList()
    }

}