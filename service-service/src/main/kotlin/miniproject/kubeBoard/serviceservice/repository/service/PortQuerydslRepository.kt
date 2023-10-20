package miniproject.kubeBoard.serviceservice.repository.service

import miniproject.kubeBoard.serviceservice.entity.service.QPortData.portData
import com.querydsl.jpa.impl.JPAQueryFactory
import miniproject.kubeBoard.serviceservice.entity.service.ServiceData
import org.springframework.stereotype.Component

@Component
class PortQuerydslRepository (
        private val queryFactory: JPAQueryFactory
){
    fun deleteAll() {
        queryFactory.delete(portData).execute()
    }

}