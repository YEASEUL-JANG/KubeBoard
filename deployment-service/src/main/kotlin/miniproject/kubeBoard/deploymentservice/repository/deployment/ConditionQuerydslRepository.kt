package miniproject.kubeBoard.deploymentservice.repository.deployment

import miniproject.kubeBoard.deploymentservice.entity.deployment.QConditionData.conditionData
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class ConditionQuerydslRepository (
        private val queryFactory: JPAQueryFactory
){

    fun deleteAll() {
        queryFactory.delete(conditionData).execute()
    }
}