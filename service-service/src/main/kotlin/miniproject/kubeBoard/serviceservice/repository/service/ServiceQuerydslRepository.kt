package miniproject.kubeBoard.serviceservice.repository.service

import miniproject.kubeBoard.serviceservice.entity.service.QServiceData.serviceData
import com.querydsl.jpa.impl.JPAQueryFactory
import miniproject.kubeBoard.serviceservice.entity.service.ServiceData
import org.springframework.stereotype.Component

@Component
class ServiceQuerydslRepository (
        private val queryFactory: JPAQueryFactory
){
    fun getService(name: String): ServiceData? {
        return queryFactory.select(serviceData)
            .from(serviceData)
            .where(serviceData.serviceName.eq(name))
            .fetchOne()
    }

    fun getSearchServiceList(search: String?, offset: Int?, sublist: Int?): List<ServiceData> {
        val searchCondition = serviceData.serviceName.containsIgnoreCase(search)
            .or(serviceData.namespace.containsIgnoreCase(search))
            .or(serviceData.labels.containsIgnoreCase(search))

        val query =  queryFactory.select(serviceData)
            .from(serviceData)
            .where(searchCondition)
        offset?.let {query.offset(it.toLong()) }
        sublist?.let {query.limit(it.toLong()) }
        query.orderBy(serviceData.createdTime.desc())
        return query.fetch()
    }


    fun deleteAll() {
        queryFactory.delete(serviceData).execute()
    }

}