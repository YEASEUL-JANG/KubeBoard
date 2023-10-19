package miniproject.kubeBoard.podservice.service

import miniproject.kubeBoard.podservice.client.PodClient
import miniproject.kubeBoard.podservice.entity.pod.res.PodListResponse
import miniproject.kubeBoard.podservice.repository.pod.PodQuerydslRepository
import miniproject.kubeBoard.podservice.repository.pod.PodRepository
import io.fabric8.kubernetes.api.model.Pod
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

}