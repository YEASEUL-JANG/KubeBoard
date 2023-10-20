package miniproject.kubeBoard.serviceservice.controller

import miniproject.kubeBoard.serviceservice.entity.service.ServiceListResponse
import miniproject.kubeBoard.serviceservice.service.ServiceService
import io.fabric8.kubernetes.api.model.Service
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
     * (client API) service 목록
     */
    @GetMapping("/client/list")
    fun getServiceClientList(): MutableList<Service>? {
        return serviceService.getServiceClientList()
    }

    /**
     * service 동기화 (30초단위 update)
     */
    @GetMapping("/batch")
    fun syncServiceList(){
        serviceService.syncServiceList()
    }

    /**
     * service 전체 데이터
     */
    @GetMapping("/listall")
    fun getServiceListAll(): ServiceListResponse {
        return serviceService.getServiceListAll()
    }

    /**
     * service 목록 데이터
     */
    @GetMapping("/list")
    fun getServiceList(
            @RequestParam(value = "search", required = false, defaultValue = "") search: String?,
            @RequestParam(value = "page", required = false, defaultValue = "1") page: Int
    ): ServiceListResponse {
        val offset= (page-1)*5
        val sublist=5*page
        return serviceService.getSearchServiceList(search,offset,sublist)
    }

    /**
     * service 상세 데이터
     */
    @GetMapping("/list/{name}")
    fun getService(
            @PathVariable("name") name:String,
    ): ServiceListResponse {
        return serviceService.getService(name)
    }

}