package com.judysen.taskscheduler.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judysen.taskscheduler.core.schedule.XxlJobDynamicScheduler;
import com.judysen.taskscheduler.core.util.OkHttpUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Scheduler;
import org.quartz.impl.calendar.AnnualCalendar;
import org.quartz.impl.calendar.DailyCalendar;
import org.quartz.impl.calendar.HolidayCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.channels.ShutdownChannelGroupException;
import java.util.*;

@Configuration
public class QuartzSchudulerConfiguration {
    @Autowired
    DataSource dataSource;

    @Value("${chineseCalendarHost}")
    private String chineseCalendarHost;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        // 延时启动
        factory.setStartupDelay(20);
        // 加载quartz数据源配置
        factory.setDataSource(dataSource);
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }
    @Bean
    public Scheduler scheduler() throws Exception {
        Scheduler scheduler=schedulerFactoryBean().getScheduler();
        AnnualCalendar chineseHoliday =(AnnualCalendar) scheduler.getCalendar("chineseHoliday");
        boolean flag=false;
        if(chineseHoliday!=null){
            Calendar currCalendar=Calendar.getInstance();
            flag=chineseHoliday.getDaysExcluded().get(0).get(Calendar.YEAR)==currCalendar.get(Calendar.YEAR);
        }else{
            chineseHoliday=new AnnualCalendar();
        }

        if(!flag){
            List<String> holidaysList=getAllHolidays();
            for(String d :holidaysList){
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(DateUtils.parseDate(d,"yyyy-MM-dd","yyyyMMdd"));
                chineseHoliday.setDayExcluded(calendar,true);
            }
            scheduler.addCalendar("chineseHoliday",chineseHoliday,true,true);
        }

        return scheduler;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    private List<String> getAllHolidays() throws IOException{
        Calendar calendar=Calendar.getInstance();
        String url=chineseCalendarHost+"/year?year="+calendar.get(Calendar.YEAR);
        String res=OkHttpUtils.init().get(url);
        if(StringUtils.isEmpty(res)){
            return new ArrayList<>();
        }
        JSONObject jsonObject= JSON.parseObject(res);
        if("1".equals(jsonObject.get("code"))){
            return (List<String>) jsonObject.get("data");
        }else{
            return new ArrayList<>();
        }
    }
    /**
     * 加载quartz数据源配置
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Properties quartzProperties(){
        try {
            PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
            propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
            propertiesFactoryBean.afterPropertiesSet();
            return propertiesFactoryBean.getObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
