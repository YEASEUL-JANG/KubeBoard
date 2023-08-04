package com.miniproject.kubeBoard.batch.config

import com.miniproject.kubeBoard.batch.run.PodBatch
import org.quartz.*
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf

@Configuration
class JobConfig(
        private val scheduler: Scheduler
) {
   // @PostConstruct
    fun run(){
        //val jobPodBatch:JobDetail = runJobDetail(PodBatch::class.java,HashMap<String,String>())
        try {
            //scheduler.scheduleJob(jobPodBatch,runJobTrigger("0/30 * * * * ?"))
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