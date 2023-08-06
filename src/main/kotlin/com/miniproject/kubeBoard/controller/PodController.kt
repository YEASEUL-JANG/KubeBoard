package com.miniproject.kubeBoard.controller

import com.miniproject.kubeBoard.entity.pod.res.PodListResponse
import com.miniproject.kubeBoard.service.PodService
import io.fabric8.kubernetes.api.model.PodList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PodController (
        private val podService: PodService
){
    /**
     * Pod 동기화 (30초단위 update)
     */
    @GetMapping("/pod/batch")
    fun syncPodList(){
        podService.syncPodList()
    }

    /**
     * Pod 전체 목록리스트
     */
    @GetMapping("/pod/list")
    fun getPodList(
            @RequestParam(value = "page", required = false, defaultValue = "1") page: Int
    ):PodListResponse{
        val offset= (page-1)*5
        val sublist=5*page
        return podService.getPodList(offset,sublist)
    }

    @GetMapping("/pod/list/{name}")
    fun getPod(
            @PathVariable("name") name:String,
    ):PodListResponse{
        return podService.getPod(name)

    }

}