package com.judysen.taskscheduler.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.judysen.taskscheduler.config.druid.PrimaryDruidConnection;
import com.judysen.taskscheduler.config.druid.SecondDruidConnection;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by judy on 2018/7/28.
 */
@Configuration
@EnableConfigurationProperties({PrimaryDruidConnection.class,SecondDruidConnection.class})
public class DruidDataSourceConfiguration {
    @Bean
    @Primary
    public DruidDataSource dataSource(PrimaryDruidConnection primaryDruidConnection) {
        return primaryDruidConnection.build();
    }
//    @Bean
//    public DruidDataSource secondDataSource(SecondDruidConnection secondDruidConnection){
//        return secondDruidConnection.build();
//    }
}
