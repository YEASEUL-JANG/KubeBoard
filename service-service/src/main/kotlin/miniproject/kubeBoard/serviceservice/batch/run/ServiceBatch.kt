package miniproject.kubeBoard.serviceservice.batch.run

import miniproject.kubeBoard.serviceservice.service.ServiceService
import lombok.extern.log4j.Log4j2
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component

@Log4j2
@Component
class ServiceBatch (
        private val schedulerFactoryBean: SchedulerFactoryBean,
        private val serviceService: ServiceService,

        ): QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        serviceService.syncServiceList()
    }
}