package miniproject.kubeBoard.serviceservice.batch.config

import miniproject.kubeBoard.serviceservice.batch.run.ServiceBatch
import org.quartz.*
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class JobConfig(
        private val scheduler: Scheduler
) {
    @PostConstruct
    fun run(){
        val jobServiceBatch:JobDetail = runJobDetail(ServiceBatch::class.java,HashMap<String,String>())
        try {
            scheduler.scheduleJob(jobServiceBatch,runJobTrigger("0/30 * * * * ?"))
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