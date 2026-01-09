package com.cg.job;


import com.cg.config.QuartzConfig;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

@PersistJobDataAfterExecution // 有状态的job，实现数据的多次共享
@DisallowConcurrentExecution // 禁止并发访问同一个job定义
//转变为定时任务中的任务类
public class Myjob implements Job {
    @Autowired
    private QuartzConfig quartzConfig;
    //实现接口方法
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //context - 定时任务执行的环境/上下文

        JobDetail jobDetail = context.getJobDetail();
//        System.out.println("System.identityHashCode(jobDetail) = " + System.identityHashCode(jobDetail));//查看jobDetail哈希码
        //输出
//        System.out.println("任务名字："+jobDetail.getKey().getName());
//        System.out.println("任务分组名字："+jobDetail.getKey().getGroup());
//        System.out.println("任务类名："+jobDetail.getJobClass().getName());
//        //具体执行由调度器控制，所以查看时间得从调度器获取
//        System.out.println("本次执行时间："+context.getFireTime());
//        System.out.println("下次执行时间："+context.getNextFireTime());
        //记录任务执行次数 从jobDataMap中获取后进行++处理再放回
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        Integer count = (Integer) jobDataMap.get("count");
        System.out.println("第"+count+"次执行");
        System.out.println("|---------------"+ quartzConfig.getJobName() +"---------------|");
        System.out.println("本次执行时间："+context.getFireTime());
        jobDataMap.put("count",++count); // 更新共享数据

        //休眠
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
}
