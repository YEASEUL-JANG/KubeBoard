package miniproject.kubeBoard.podservice.controller

import miniproject.kubeBoard.podservice.entity.pod.res.PodListResponse
import miniproject.kubeBoard.podservice.service.PodService
import io.fabric8.kubernetes.api.model.Pod
import miniproject.kubeBoard.podservice.entity.pod.req.PodCreateRequest
import miniproject.kubeBoard.podservice.entity.pod.req.PodDeleteRequest
import org.springframework.web.bind.annotation.*
import kotlin.math.log

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
     * pod 생성
     */
    @PostMapping("/create")
    fun createPod(@RequestBody podCreateRequest: PodCreateRequest): String {
        return podService.createPod(podCreateRequest)
    }
    /**
     * pod 삭제
     */
    @PostMapping("/delete")
    fun deletePod(@RequestBody podDeleteRequest: PodDeleteRequest): Boolean {
        return podService.deletePod(podDeleteRequest)
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