package com.miniproject.kubeBoard.controller

import com.miniproject.kubeBoard.entity.deployment.res.DeploymentListResponse
import com.miniproject.kubeBoard.entity.pod.res.PodListResponse
import com.miniproject.kubeBoard.entity.service.ServiceListResponse
import com.miniproject.kubeBoard.service.DeploymentService
import com.miniproject.kubeBoard.service.PodService
import com.miniproject.kubeBoard.service.ServiceService
import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.api.model.Service
import io.fabric8.kubernetes.api.model.apps.Deployment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/service")
class ServiceController (
        private val serviceService: ServiceService
){
    /**
     * (client API) deployment 목록
     */
    @GetMapping("/client/list")
    fun getServiceClientList(): MutableList<Service>? {
        return serviceService.getServiceClientList()
    }

    /**
     * deployment 동기화 (30초단위 update)
     */
    @GetMapping("/batch")
    fun syncDeploymentList(){
        serviceService.syncDeploymentList()
    }

    /**
     * deployment 전체 데이터
     */
    @GetMapping("/listall")
    fun getServiceListAll(): ServiceListResponse {
        return serviceService.getServiceListAll()
    }

//    /**
//     * deployment 목록 데이터
//     */
//    @GetMapping("/list")
//    fun getDeploymentList(
//            @RequestParam(value = "page", required = false, defaultValue = "1") page: Int
//    ): ServiceListResponse {
//        val offset= (page-1)*5
//        val sublist=5*page
//        return serviceService.getServiceList(offset,sublist)
//    }
//
//    /**
//     * deployment 상세 데이터
//     */
//    @GetMapping("/list/{name}")
//    fun getPod(
//            @PathVariable("name") name:String,
//    ): DeploymentListResponse {
//        return serviceService.getService(name)
//    }
}