package miniproject.kubeBoard.podservice.client

import miniproject.kubeBoard.podservice.entity.pod.PodData
import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.client.KubernetesClient
import miniproject.kubeBoard.podservice.entity.pod.req.PodCreateRequest
import org.springframework.stereotype.Service
import io.fabric8.kubernetes.api.model.PodBuilder;
import miniproject.kubeBoard.podservice.entity.pod.req.PodDeleteRequest

@Service
class PodClient(
        private val client: KubernetesClient
) {
    fun getPodList():List<PodData>{
        val podList = mutableListOf<PodData>()
        val pods = client.pods()?.list()?.items
        if(pods != null){
            for(pod in pods){
                podList.add(PodData.of(pod))
            }
        }
        return podList
    }
    fun getPodClientList(): MutableList<Pod>? {
        return client.pods()?.list()?.items
    }

    fun createPod(podCreateRequest: PodCreateRequest) {
        client.pods().inNamespace(podCreateRequest.namespace).create(
            PodBuilder()
                .withNewMetadata()
                .withName(podCreateRequest.name)
                .endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName("nginx")
                .withImage("nginx:1.14")
                .endContainer()
                .endSpec()
                .build()
        )
    }

    fun deletePod(podDeleteRequest: PodDeleteRequest) {
        client.pods().inNamespace(podDeleteRequest.namespace).withName(podDeleteRequest.name).delete()
    }

    fun getPodStatus(namespace: String, name: String): String? {
        val getPod = client.pods().inNamespace(namespace).withName(name).get()
        if(getPod != null){
            return getPod.status.phase
        }
        return null
    }

    fun getLabels(): Map<String, Set<String>> {
        val pods: List<Pod> = client.pods().inNamespace("default").list().items

        return pods.mapNotNull { pod ->
            pod.metadata?.labels?.entries
        }.flatten().groupBy(
            { it.key },
            { it.value }
        ).mapValues { (_, values) -> values.toSet()}
    }
}