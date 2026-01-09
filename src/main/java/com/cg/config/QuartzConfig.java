package com.cg.config;

import com.cg.job.Myjob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//配置类
@Configuration
public class QuartzConfig {
    /**
     * 任务名字
     */
    private String jobName = "myjob";

    public String getJobName() {
        return jobName;
    }

    //JObDetail的创建
    @Bean(name = "myJobDetail")
    public JobDetail jobDetail(){
        //与Myjob进行绑定
        return JobBuilder.newJob(Myjob.class)
                // 当没有Trigger与JobDetail相关联时，JobDetail就会被删除，有了持久化则不会
                .storeDurably() //持久化（非必要）
                .withIdentity(jobName)//唯一标识，用来区分隔离其他任务
                .usingJobData("count",1)//某个数据需要多次共享时，可以对该数据进行初始化。
                .build();
    }
    //Trigger的创建
    @Bean
    public Trigger trigger(@Qualifier(value = "myJobDetail") JobDetail jobDetail){
        //cron表达式
        String croExpression = "0/5 * * * * ? *"; //每隔2分钟执行一次
        //基于cron表达式的调度构建器
        //补偿策略
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(croExpression).withMisfireHandlingInstructionFireAndProceed();
        return TriggerBuilder.newTrigger()
//                .startAt(DateBuilder.todayAt(10,45,0))             //定时任务的初次执行时间（开始时间）
                .withIdentity(jobName+"_trigger")     //唯一标识
                .forJob(jobDetail())                        //定义该触发器为哪个JobDetail服务，将Tragger与jobDetail相关联
                .withSchedule(cronScheduleBuilder)          // 调度构建器
                .build();
    }
}
