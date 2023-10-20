package miniproject.kubeBoard.deploymentservice.batch.run

import miniproject.kubeBoard.deploymentservice.service.DeploymentService
import lombok.extern.log4j.Log4j2
import org.quartz.JobExecutionContext
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Component

@Log4j2
@Component
class DeploymentBatch (
        private val schedulerFactoryBean: SchedulerFactoryBean,
        private val deploymentService: DeploymentService,

        ): QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        deploymentService.syncDeploymentList()
    }
}