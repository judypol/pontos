package com.judysen.remoteexecutor;

import com.judysen.remoteexecutor.jobhandler.TaskMLTPDeliveryImpl;
import com.judysen.remoteexecutor.jobhandler.TaskMLTPNormalImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MLTPDeliveryImplTest {
    @Autowired
    private TaskMLTPDeliveryImpl taskMLTPDelivery;
    @Autowired
    private TaskMLTPNormalImpl taskMLTPNormal;
    @Test
    public void deliveryImpl() throws Exception{
        String param="{\"methodname\":\"cancelLeads\",\"servicename\":\"cmltp.job.service.IJobMgrService\",\"message\":\"测试\",\"receiver\":\"lizhihua@shcem.com\"}";
        taskMLTPDelivery.execute(param);
    }
    @Test
    public void normalImpl() throws Exception{
        String param="{\"methodname\":\"cancelLeads\",\"servicename\":\"cmltp.job.service.IJobMgrService\",\"message\":\"测试\",\"receiver\":\"lizhihua@shcem.com\"}";
        taskMLTPNormal.execute(param);
    }
}