package miniproject.kubeBoard.podservice.repository.pod

import miniproject.kubeBoard.podservice.entity.pod.QContainerData.containerData
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class ContainerQuerydslRepository (
        private val queryFactory: JPAQueryFactory
){

    fun deleteAll() {
        queryFactory.delete(containerData).execute()
    }
}