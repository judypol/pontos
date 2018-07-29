package com.judysen.remoteexecutor.config;

import com.xxl.job.core.executor.XxlJobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * pontos-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
@ComponentScan(basePackages = "com.judysen.remoteexecutor.jobhandler")
public class PontosJobConfig {
    private Logger logger = LoggerFactory.getLogger(PontosJobConfig.class);

    @Value("${pontos.job.admin.addresses}")
    private String adminAddresses;

    @Value("${pontos.job.executor.appname}")
    private String appName;

    @Value("${pontos.job.executor.ip}")
    private String ip;

    @Value("${pontos.job.executor.port}")
    private int port;

    @Value("${pontos.job.accessToken}")
    private String accessToken;

    @Value("${pontos.job.executor.logpath}")
    private String logPath;

    @Value("${pontos.job.executor.logretentiondays}")
    private int logRetentionDays;

    /**
     * 初始化一个执行器，可以包括多个handler
     * @return
     */
    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> pontos-job config init.");
        XxlJobExecutor xxlJobExecutor = new XxlJobExecutor();
        xxlJobExecutor.setAdminAddresses(adminAddresses);
        xxlJobExecutor.setAppName(appName);
        xxlJobExecutor.setIp(ip);
        xxlJobExecutor.setPort(port);
        xxlJobExecutor.setAccessToken(accessToken);
        xxlJobExecutor.setLogPath(logPath);
        xxlJobExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobExecutor;
    }

}