package com.judysen.taskscheduler.config.druid;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by judy on 2018/7/29.
 */
@ConfigurationProperties(prefix = "spring.datasource.primary")
public class PrimaryDruidConnection extends DruidDataSourceProperties {
}
