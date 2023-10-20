package miniproject.kubeBoard.deploymentservice.controller

import miniproject.kubeBoard.deploymentservice.entity.deployment.res.DeploymentListResponse
import miniproject.kubeBoard.deploymentservice.service.DeploymentService
import io.fabric8.kubernetes.api.model.apps.Deployment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
    @GetMapping("/scale")
    fun getDeploymentScale(
            @RequestParam("name") name: String,
            @RequestParam("namespace") namespace: String,
            @RequestParam("scale") scale: Int,
    ):Int{
        return if(!name.equals("")&& !namespace.equals("") && scale!=0){
            deploymentService.changeReplica(name,namespace,scale)
            1;
        }else 0;
    }
}