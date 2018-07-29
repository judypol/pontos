package com.judysen.taskscheduler.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

/**
 * Created by judy on 2018/7/28.
 */
@Setter
@Getter
public class DruidDataSourceProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private String dbType;
    private Boolean testWhileIdle = true;
    private Boolean testOnBorrow;
    private String validationQuery = "SELECT 1";
    private Boolean useGlobalDataSourceStat;
    private String filters;
    private Long timeBetweenLogStatsMillis;
    private Integer maxSize;
    private Boolean clearFiltersEnable;
    private Boolean resetStatEnable;
    private Integer notFullTimeoutRetryCount;
    private Integer maxWaitThreadCount;
    private Boolean failFast;
    private Boolean phyTimeoutMillis;
    private Long minEvictableIdleTimeMillis = 300000L;
    private Long maxEvictableIdleTimeMillis;
    private Integer initialSize = 5;
    private Integer minIdle = 5;
    private Integer maxActive = 20;
    private Long maxWait = 60000L;
    private Long timeBetweenEvictionRunsMillis = 60000L;
    private Boolean poolPreparedStatements = true;
    private Integer maxPoolPreparedStatementPerConnectionSize = 20;
    private Properties connectionProperties = new Properties() {{
        put("druid.stat.mergeSql", "true");
        put("druid.stat.slowSqlMillis", "5000");
    }};

    public Properties toProperties() {
        Properties properties = new Properties();
        notNullAdd(properties, "testWhileIdle", this.testWhileIdle);
        notNullAdd(properties, "testOnBorrow", this.testOnBorrow);
        notNullAdd(properties, "validationQuery", this.validationQuery);
        notNullAdd(properties, "useGlobalDataSourceStat", this.useGlobalDataSourceStat);
        notNullAdd(properties, "filters", this.filters);
        notNullAdd(properties, "timeBetweenLogStatsMillis", this.timeBetweenLogStatsMillis);
        notNullAdd(properties, "stat.sql.MaxSize", this.maxSize);
        notNullAdd(properties, "clearFiltersEnable", this.clearFiltersEnable);
        notNullAdd(properties, "resetStatEnable", this.resetStatEnable);
        notNullAdd(properties, "notFullTimeoutRetryCount", this.notFullTimeoutRetryCount);
        notNullAdd(properties, "maxWaitThreadCount", this.maxWaitThreadCount);
        notNullAdd(properties, "failFast", this.failFast);
        notNullAdd(properties, "phyTimeoutMillis", this.phyTimeoutMillis);
        notNullAdd(properties, "minEvictableIdleTimeMillis", this.minEvictableIdleTimeMillis);
        notNullAdd(properties, "maxEvictableIdleTimeMillis", this.maxEvictableIdleTimeMillis);
        notNullAdd(properties, "initialSize", this.initialSize);
        notNullAdd(properties, "minIdle", this.minIdle);
        notNullAdd(properties, "maxActive", this.maxActive);
        notNullAdd(properties, "maxWait", this.maxWait);
        notNullAdd(properties, "timeBetweenEvictionRunsMillis", this.timeBetweenEvictionRunsMillis);
        notNullAdd(properties, "poolPreparedStatements", this.poolPreparedStatements);
        notNullAdd(properties, "maxPoolPreparedStatementPerConnectionSize", this.maxPoolPreparedStatementPerConnectionSize);
        return properties;
    }

    /**
     * 生成一个Druid连接
     * @param
     * @return
     */
    public DruidDataSource build(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.configFromPropety(this.toProperties());
        druidDataSource.setInitialSize(this.getInitialSize());
        druidDataSource.setDbType(this.getDbType());
        druidDataSource.setUrl(this.getUrl());
        druidDataSource.setDriverClassName(this.getDriverClassName());
        druidDataSource.setUsername(this.getUsername());
        druidDataSource.setPassword(this.getPassword());
        druidDataSource.setMinIdle(this.getMinIdle());
        druidDataSource.setMaxActive(this.getMaxActive());
        druidDataSource.setMaxWait(this.getMaxWait());
        druidDataSource.setConnectProperties(this.getConnectionProperties());
        return druidDataSource;
    }

    private void notNullAdd(Properties properties, String key, Object value) {
        if (value != null) {
            properties.setProperty("druid." + key, value.toString());
        }
    }
}
