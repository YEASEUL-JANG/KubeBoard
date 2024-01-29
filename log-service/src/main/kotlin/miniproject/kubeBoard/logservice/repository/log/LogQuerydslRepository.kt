package miniproject.kubeBoard.logservice.repository.log

import com.querydsl.jpa.impl.JPAQueryFactory
import miniproject.kubeBoard.logservice.entity.log.LogData
import miniproject.kubeBoard.logservice.entity.log.QLogData.logData
import org.springframework.stereotype.Component

@Component
class LogQuerydslRepository (
        private val queryFactory: JPAQueryFactory
){
    fun findAllByUserId(userId: String?, offset: Int?, sublist: Int?): List<LogData> {

        val query =  queryFactory.select(logData)
                .from(logData)
                .where(logData.userId.eq(userId))
                offset?.let {query.offset(it.toLong()) }
                sublist?.let {query.limit(it.toLong()) }

        query.orderBy(logData.requestedTime.desc())
        return query.fetch()
    }

}