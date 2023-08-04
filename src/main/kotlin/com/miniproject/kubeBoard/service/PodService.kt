package com.miniproject.kubeBoard.service

import com.miniproject.kubeBoard.client.PodClient
import com.miniproject.kubeBoard.entity.PodData
import com.miniproject.kubeBoard.repository.PodRepository
import io.fabric8.kubernetes.client.KubernetesClient
import io.fabric8.kubernetes.client.KubernetesClientBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PodService (
        private val podRepository: PodRepository,
        private val podClient: PodClient,
){
    @Transactional
    fun syncPodList(){
        //기존 podList 전체 삭제
        podRepository.deleteAll()
        //동기화해온 리스트 저장
        val podList= podClient.getPodList()
        podRepository.saveAll(podList)
    }

}