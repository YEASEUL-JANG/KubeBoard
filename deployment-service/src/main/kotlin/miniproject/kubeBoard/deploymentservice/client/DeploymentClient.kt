package miniproject.kubeBoard.deploymentservice.client

import miniproject.kubeBoard.deploymentservice.entity.deployment.DeploymentData
import io.fabric8.kubernetes.api.model.apps.Deployment
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder
import io.fabric8.kubernetes.client.KubernetesClient
import lombok.extern.slf4j.Slf4j
import miniproject.kubeBoard.podservice.entity.pod.req.DeploymentCreateRequest
import miniproject.kubeBoard.podservice.entity.pod.req.DeploymentDeleteRequest
import miniproject.kubeBoard.podservice.entity.pod.req.DeploymentScaleRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
@Slf4j
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

    fun changeReplica(deploymentScaleRequest : DeploymentScaleRequest) {
        try{
            client.apps().deployments().inNamespace(deploymentScaleRequest.namespace)
                .withName(deploymentScaleRequest.name)
                .scale(deploymentScaleRequest.scale)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun getDeploymentStatus(namespace: String, name: String): Boolean {
        val deployment = client.apps().deployments().inNamespace(namespace).withName(name).get() ?: return false
        if (deployment.spec.replicas == deployment.status.availableReplicas) {
            for (condition in deployment.status.conditions) {
                if (condition.type == "Available" && condition.status == "True") {
                    return true
                }
            }
        }
        return false
    }

    fun createDeployment(deploymentCreateRequest: DeploymentCreateRequest) {
            val deploymentBuilder = DeploymentBuilder()
                .withNewMetadata()
                .withName(deploymentCreateRequest.name)
                .addToLabels("app",deploymentCreateRequest.name)
                .endMetadata()
                .withNewSpec()
                .withReplicas(deploymentCreateRequest.replica)
                .withNewTemplate()
                .withNewMetadata()
                .addToLabels("app", "nginx")
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("nginx")
                .withImage("nginx")
                .addNewPort()
                .withContainerPort(80)
                .endPort()
                .endContainer()
                .endSpec()
                .endTemplate()
                .withNewSelector()
                .addToMatchLabels("app", "nginx")
                .endSelector()
                .endSpec()
                .build();

            client.apps().deployments().inNamespace(deploymentCreateRequest.namespace).resource(deploymentBuilder).create()
    }

    fun deleteDeployment(deploymentDeleteRequest: DeploymentDeleteRequest) {
        client.apps().deployments().inNamespace(deploymentDeleteRequest.namespace).withName(deploymentDeleteRequest.name).delete()
    }
}