package com.miniproject.kubeBoard.controller

import com.miniproject.kubeBoard.entity.pod.res.PodListResponse
import com.miniproject.kubeBoard.service.PodService
import io.fabric8.kubernetes.api.model.Pod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/pod")
class PodController (
        private val podService: PodService
){
    /**
     * pod 목록 (client API)
     */
    @GetMapping("/client/list")
    fun getPodClientList(): MutableList<Pod>? {
        return podService.getPodClientList()
    }

    /**
     * Pod 동기화 (30초단위 update)
     */
    @GetMapping("/batch")
    fun syncPodList(){
        podService.syncPodList()
    }

    /**
     * Pod 전체 데이터
     */
    @GetMapping("/listall")
    fun getPodListAll(): PodListResponse {
        return podService.getPodListAll()
    }

    /**
     * Pod 목록 데이터
     */
    @GetMapping("/list")
    fun getPodList(
            @RequestParam(value = "page", required = false, defaultValue = "1") page: Int
    ): PodListResponse {
        val offset= (page-1)*5
        val sublist=5*page
        return podService.getPodList(offset,sublist)
    }

    @GetMapping("/list/{name}")
    fun getPod(
            @PathVariable("name") name:String,
    ): PodListResponse {
        return podService.getPod(name)

    }
}