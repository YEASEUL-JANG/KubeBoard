package com.miniproject.kubeBoard.controller

import com.miniproject.kubeBoard.entity.pod.res.PodListResponse
import com.miniproject.kubeBoard.service.PodService
import io.fabric8.kubernetes.api.model.PodList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PodController (
        private val podService: PodService
){
    @GetMapping("/pod/batch")
    fun syncPodList(){
        podService.syncPodList()
    }

    @GetMapping("/pod/list")
    fun getPodList(
            @RequestParam page: Int
    ):PodListResponse{
        val offset= (page-1)*5
        val sublist=5*page
        return podService.getPodList(offset,sublist)
    }

}