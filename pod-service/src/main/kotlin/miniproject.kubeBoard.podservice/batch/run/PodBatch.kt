package miniproject.kubeBoard.podservice.batch.run

import miniproject.kubeBoard.podservice.service.PodService
import lombok.extern.log4j.Log4j2
import org.quartz.JobExecutionContext
import org.quartz.JobKey
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component

@Log4j2
@Component
class PodBatch (
        private val schedulerFactoryBean: SchedulerFactoryBean,
        private val podService: PodService,

        ): QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        podService.syncPodList()
    }
}