package com.miniproject.kubeBoard.repository.pod

import com.miniproject.kubeBoard.entity.pod.PodData
import com.miniproject.kubeBoard.entity.pod.QPodData.podData
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

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
}