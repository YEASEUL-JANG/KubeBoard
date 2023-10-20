package miniproject.kubeBoard.deploymentservice.client

import miniproject.kubeBoard.deploymentservice.entity.deployment.DeploymentData
import io.fabric8.kubernetes.api.model.apps.Deployment
import io.fabric8.kubernetes.client.KubernetesClient
import org.springframework.stereotype.Service

@Service
class DeploymentClient(
        private val client: KubernetesClient
) {
    fun getDeploymentList():List<DeploymentData>{
        val deploymentList = mutableListOf<DeploymentData>()
        val deployments = client.apps().deployments()?.list()?.items
        if(deployments != null){
            for(deployment in deployments){
                deploymentList.add(DeploymentData.of(deployment))
            }
        }
        return deploymentList
    }
    fun getDeploymentClientList(): MutableList<Deployment>? {
        return client.apps().deployments()?.list()?.items
    }

    fun changeReplica(name: String, namespace: String, scale: Int) {
        try{
            client.apps().deployments().inNamespace(namespace).withName(name).scale(scale)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}