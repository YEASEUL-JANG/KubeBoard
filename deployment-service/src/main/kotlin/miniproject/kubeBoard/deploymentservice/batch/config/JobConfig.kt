package miniproject.kubeBoard.deploymentservice.batch.config

import miniproject.kubeBoard.deploymentservice.batch.run.DeploymentBatch
import org.quartz.*
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class JobConfig(
        private val scheduler: Scheduler
) {
    @PostConstruct
    fun run(){
        val jobDeploymentBatch:JobDetail = runJobDetail(DeploymentBatch::class.java,HashMap<String,String>())
        try {
            scheduler.scheduleJob(jobDeploymentBatch,runJobTrigger("0/30 * * * * ?"))
        }catch (e: SchedulerException){
            e.printStackTrace()
        }
    }
    fun runJobDetail(job:Class<out Job>, params:Map<String,String>): JobDetail{
        val jobDataMap = JobDataMap()
        jobDataMap.putAll(params)
        return JobBuilder.newJob(job).usingJobData(jobDataMap).build()
    }
    fun runJobTrigger(schedulerExp:String):Trigger{
        return TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(schedulerExp)).build()
    }
}