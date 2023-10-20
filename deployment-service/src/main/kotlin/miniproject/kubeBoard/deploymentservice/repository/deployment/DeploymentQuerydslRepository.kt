package miniproject.kubeBoard.deploymentservice.repository.deployment

import miniproject.kubeBoard.deploymentservice.entity.deployment.DeploymentData
import miniproject.kubeBoard.deploymentservice.entity.deployment.QDeploymentData.deploymentData
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class DeploymentQuerydslRepository (
        private val queryFactory: JPAQueryFactory
){
    fun getDeployment(name: String): DeploymentData? {
        return queryFactory.select(deploymentData)
                .from(deploymentData)
                .where(deploymentData.deploymentName.eq(name))
                .fetchOne()
    }

    fun getSearchDeploymentList(search: String?, offset: Int?, sublist: Int?): List<DeploymentData> {
        val searchCondition = deploymentData.deploymentName.containsIgnoreCase(search)
                .or(deploymentData.namespace.containsIgnoreCase(search))
                .or(deploymentData.labels.containsIgnoreCase(search))

        val query =  queryFactory.select(deploymentData)
                .from(deploymentData)
                .where(searchCondition)
        offset?.let {query.offset(it.toLong()) }
        sublist?.let {query.limit(it.toLong()) }
        query.orderBy(deploymentData.createdTime.desc())
        return query.fetch()
    }

    fun deleteAll() {
        queryFactory.delete(deploymentData).execute()
    }
}