package miniproject.kubeBoard.deploymentservice.controller

import miniproject.kubeBoard.deploymentservice.entity.deployment.res.DeploymentListResponse
import miniproject.kubeBoard.deploymentservice.service.DeploymentService
import io.fabric8.kubernetes.api.model.apps.Deployment
import miniproject.kubeBoard.podservice.entity.pod.req.DeploymentCreateRequest
import miniproject.kubeBoard.podservice.entity.pod.req.DeploymentDeleteRequest
import miniproject.kubeBoard.podservice.entity.pod.req.DeploymentScaleRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/deployment-service")
class DeploymentController (
        private val deploymentService: DeploymentService
){
    /**
     * (client API) deployment 목록
     */
    @GetMapping("/client/list")
    fun getDeploymentClientList(): MutableList<Deployment>? {
        return deploymentService.getDeploymentClientList()
    }

    /**
     * deployment 생성
     */
    @PostMapping("/create")
    fun createPod(@RequestBody deploymentCreateRequest: DeploymentCreateRequest): String {
        return deploymentService.createDeployment(deploymentCreateRequest)
    }
    /**
     * deployment 삭제
     */
    @PostMapping("/delete")
    fun deletePod(@RequestBody deploymentDeleteRequest: DeploymentDeleteRequest): Boolean {
        return deploymentService.deleteDeployment(deploymentDeleteRequest)
    }

    /**
     * deployment 동기화 (30초단위 update)
     */
    @GetMapping("/batch")
    fun syncDeploymentList(){
        deploymentService.syncDeploymentList()
    }

    /**
     * deployment 전체 데이터
     */
    @GetMapping("/listall")
    fun getDeploymentListAll(): DeploymentListResponse {
        return deploymentService.getDeploymentListAll()
    }

    /**
     * deployment 목록 데이터
     */
    @GetMapping("/list")
    fun getDeploymentList(
            @RequestParam(value="search", required = false, defaultValue = "") search:String?,
            @RequestParam(value = "page", required = false, defaultValue = "1") page: Int
    ): DeploymentListResponse {
        val offset= (page-1)*5
        val sublist=5*page
        return deploymentService.getSearchDeployment(search,offset,sublist)
    }

    /**
     * deployment 상세 데이터
     */
    @GetMapping("/list/{name}")
    fun getPod(
            @PathVariable("name") name:String,
    ): DeploymentListResponse {
        return deploymentService.getDeployment(name)

    }
    @PostMapping("/scale")
    fun getDeploymentScale(
            @RequestBody deploymentScaleRequest: DeploymentScaleRequest,
    ):Int{
        return if(!deploymentScaleRequest.name.equals("") &&
            !deploymentScaleRequest.namespace.equals("") &&
            deploymentScaleRequest.scale!=0){
            deploymentService.changeReplica(deploymentScaleRequest)
            deploymentService.savelogData(
                deploymentScaleRequest.name,
                deploymentScaleRequest.scale,
                "replica",
                "replica scale",
                deploymentScaleRequest.userId
            )
            1;
        }else 0;
    }
}