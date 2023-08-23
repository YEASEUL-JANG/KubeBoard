package com.miniproject.kubeBoard.repository.pod

import com.miniproject.kubeBoard.entity.pod.PodData
import com.miniproject.kubeBoard.entity.pod.QPodData.podData
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.function.Predicate

@Component
class PodQuerydslRepository (
        private val queryFactory: JPAQueryFactory
){
    fun getPodList(offset: Int, sublist: Int):List<PodData> {
        return queryFactory.select(podData)
                .from(podData)
                .offset(offset.toLong())
                .limit(sublist.toLong())
                .fetch()
    }

    fun getPod(name: String): PodData? {
        return queryFactory.select(podData)
                .from(podData)
                .where(podData.podName.eq(name))
                .fetchOne()
    }

    fun getSearchPodList(name: String, offset: Int?, sublist: Int?): List<PodData> {
        val searchCondition = podData.podName.containsIgnoreCase(name)
                .or(podData.namespace.containsIgnoreCase(name))
                .or(podData.nodeName.containsIgnoreCase(name))
                .or(podData.labels.containsIgnoreCase(name))

        val query =  queryFactory.select(podData)
                .from(podData)
                .where(searchCondition)
                offset?.let {query.offset(it.toLong()) }
                sublist?.let {query.limit(it.toLong()) }
        return query.fetch()
    }
}