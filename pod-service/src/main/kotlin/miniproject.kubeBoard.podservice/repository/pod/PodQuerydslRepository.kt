package miniproject.kubeBoard.podservice.repository.pod

import miniproject.kubeBoard.podservice.entity.pod.PodData
import miniproject.kubeBoard.podservice.entity.pod.QPodData.podData
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class PodQuerydslRepository (
        private val queryFactory: JPAQueryFactory
){
    fun getPod(name: String): PodData? {
        return queryFactory.select(podData)
                .from(podData)
                .where(podData.podName.eq(name))
                .fetchOne()
    }

    fun getSearchPodList(search: String?, offset: Int?, sublist: Int?): List<PodData> {
        val searchCondition = podData.podName.containsIgnoreCase(search)
                .or(podData.namespace.containsIgnoreCase(search))
                .or(podData.nodeName.containsIgnoreCase(search))
                .or(podData.labels.containsIgnoreCase(search))

        val query =  queryFactory.select(podData)
                .from(podData)
                .where(searchCondition)
                offset?.let {query.offset(it.toLong()) }
                sublist?.let {query.limit(it.toLong()) }

        query.orderBy(podData.createdTime.desc())
        return query.fetch()
    }

    fun deleteAll() {
        queryFactory.delete(podData).execute()
    }
}