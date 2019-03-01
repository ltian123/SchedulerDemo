package cn.ltian.base;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MyScheduler {

    //每10秒执行一次
    @Scheduled(cron = "0/2 * * * * ?")
    @Async(value="MyScheduler")
    public void aTask() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date())+"     :"+Thread.currentThread().getName()+"a任务执行一次,2m 开始");
       try{
           Thread.sleep(5000);
       }catch(Exception e){
       }
        System.out.println(sdf.format(new Date())+"     :"+Thread.currentThread().getName()+"a任务执行一次,2m 结束");
    }


    //每5秒执行一次
    @Scheduled(cron = "0/5 * * * * ?")
    public void bTask() {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date())+"     :"+Thread.currentThread().getName()+"b任务执行一次,5m");
    }
}