package com.miniproject.kubeBoard.batch.config

import com.miniproject.kubeBoard.batch.run.DeploymentBatch
import com.miniproject.kubeBoard.batch.run.PodBatch
import com.miniproject.kubeBoard.batch.run.ServiceBatch
import org.quartz.*
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf

@Configuration
class JobConfig(
        private val scheduler: Scheduler
) {
    @PostConstruct
    fun run(){
        val jobPodBatch:JobDetail = runJobDetail(PodBatch::class.java,HashMap<String,String>())
        val jobDeploymentBatch:JobDetail = runJobDetail(DeploymentBatch::class.java,HashMap<String,String>())
        val jobServiceBatch:JobDetail = runJobDetail(ServiceBatch::class.java,HashMap<String,String>())
        try {
            scheduler.scheduleJob(jobPodBatch,runJobTrigger("0/30 * * * * ?"))
            scheduler.scheduleJob(jobDeploymentBatch,runJobTrigger("0/30 * * * * ?"))
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