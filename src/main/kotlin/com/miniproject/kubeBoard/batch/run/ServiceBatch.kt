package com.miniproject.kubeBoard.batch.run

import com.miniproject.kubeBoard.service.DeploymentService
import com.miniproject.kubeBoard.service.PodService
import com.miniproject.kubeBoard.service.ServiceService
import lombok.extern.log4j.Log4j2
import org.quartz.JobExecutionContext
import org.quartz.JobKey
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