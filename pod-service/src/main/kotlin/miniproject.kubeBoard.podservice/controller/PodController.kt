package miniproject.kubeBoard.podservice.controller

import miniproject.kubeBoard.podservice.entity.pod.res.PodListResponse
import miniproject.kubeBoard.podservice.service.PodService
import io.fabric8.kubernetes.api.model.Pod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pod-service")
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
            @RequestParam(value = "search", required = false, defaultValue = "")search: String?,
            @RequestParam(value = "page", required = false, defaultValue = "1") page: Int
    ): PodListResponse {
        val offset= (page-1)*5
        val sublist=5*page
        return podService.getSearchPod(search,offset,sublist)
    }

    /**
     * Pod 상세 데이터
     */
    @GetMapping("/list/{name}")
    fun getPod(
            @PathVariable("name") name:String,
    ): PodListResponse {
        return podService.getPod(name)

    }
}