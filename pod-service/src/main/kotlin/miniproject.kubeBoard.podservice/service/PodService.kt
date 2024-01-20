package miniproject.kubeBoard.podservice.service

import miniproject.kubeBoard.podservice.client.PodClient
import miniproject.kubeBoard.podservice.entity.pod.res.PodListResponse
import miniproject.kubeBoard.podservice.repository.pod.PodQuerydslRepository
import miniproject.kubeBoard.podservice.repository.pod.PodRepository
import io.fabric8.kubernetes.api.model.Pod
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import miniproject.kubeBoard.podservice.entity.pod.req.PodCreateRequest
import miniproject.kubeBoard.podservice.entity.pod.req.PodDeleteRequest
import miniproject.kubeBoard.podservice.entity.pod.req.UserLogRequest
import miniproject.kubeBoard.podservice.kafka.KafkaProducer
import miniproject.kubeBoard.podservice.repository.pod.ContainerQuerydslRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
class PodService (
    private val podRepository: PodRepository,
    private val podClient: PodClient,
    private val podQuerydslRepository: PodQuerydslRepository,
    private val containerQuerydslRepository: ContainerQuerydslRepository,
    private val entityManager: EntityManager,
    private val kafkaProducer: KafkaProducer
){
    @Transactional
    fun syncPodList(){
        //기존 podList, containerList 일괄 삭제
        containerQuerydslRepository.deleteAll()
        podQuerydslRepository.deleteAll()
        //동기화해온 리스트 일괄 저장
        val podList= podClient.getPodList()
        for(pod in podList){
            entityManager.persist(pod)
        }
    }

    fun getPod(name: String): PodListResponse {
        val pod = podQuerydslRepository.getPod(name)
        return PodListResponse(1, listOf(pod))
    }

    fun getPodListAll(): PodListResponse {
        val podList = podRepository.findAll()
        return PodListResponse(podList.size,podList);
    }

    fun getPodClientList(): MutableList<Pod>? {
        return podClient.getPodClientList()
    }

    fun getSearchPod(search: String?, offset: Int, sublist: Int): PodListResponse {
        val podList = podQuerydslRepository.getSearchPodList(search,offset,sublist)
        val count = podQuerydslRepository.getSearchPodList(search,null,null).size
        return PodListResponse(count,podList)
    }
    fun getPodStatus(namespace: String, name: String): String? {
        return podClient.getPodStatus(namespace, name);
    }

    @Transactional
    fun createPod(podCreateRequest: PodCreateRequest): String {
        podClient.createPod(podCreateRequest);
        val maxAttempts = 10
        var currentAttempt = 0
        runBlocking {
            while (currentAttempt < maxAttempts){
                val status = getPodStatus(podCreateRequest.namespace,podCreateRequest.name)
                if(status.equals("Running", ignoreCase = true)) {
                    syncPodList()
                    break
                }
            }
            delay(2000)
            currentAttempt++
             }
        //로그 저장
        savelogData(
                podCreateRequest.name,
                1,
                "pod",
                "create",
                podCreateRequest.userId
        )
        return podCreateRequest.name
    }
    @Transactional
    fun deletePod(podDeleteRequest: PodDeleteRequest): Boolean {
        podClient.deletePod(podDeleteRequest)
        val maxAttempts = 10
        var currentAttempt = 0
        var isPodDeleted = false
        runBlocking {
            while (currentAttempt < maxAttempts){
                val status = getPodStatus(podDeleteRequest.namespace,podDeleteRequest.name)
                if(status == null){
                    isPodDeleted = true
                    syncPodList()
                    break
                }
                delay(2000)
                currentAttempt++
            }
        }
        //로그 저장
        savelogData(
                podDeleteRequest.name,
                1,
                "deployment",
                "delete",
                podDeleteRequest.userId
        )
        return isPodDeleted;
    }

    fun getLabels(): List<String> {
        val labelsMap: Map<String, Set<String>> = podClient.getLabels()
        val labelsList = mutableListOf<String>()

        labelsMap.forEach { (key, values) ->
            values.forEach { value ->
                labelsList.add("$key=$value")
            }
        }

        return labelsList
    }
    fun savelogData(name: String, scale: Int, requestData: String,
                    requestSource: String, userId: String) {
        val userLogRequest = UserLogRequest(
                userId = userId,
                requestMs = "deployment-service",
                requestData = requestData,
                requestSource = requestSource,
                requestNum = scale
        )
        kafkaProducer.send("log-service",userLogRequest)
    }

}