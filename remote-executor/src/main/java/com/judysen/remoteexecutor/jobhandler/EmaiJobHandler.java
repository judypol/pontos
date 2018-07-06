package com.judysen.remoteexecutor.jobhandler;

import com.judysen.remoteexecutor.core.RemoteServiceInvoke;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@JobHandler(value="emaiJobHandler")
@Component
public class EmaiJobHandler extends IJobHandler {

    @Value("emai.middle.url")
    private String Url;
    /**
     * execute handler, invoked when executor receives a scheduling request
     *
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("EmaiJobHandler Start, the Param is "+param);
        ReturnT ret= RemoteServiceInvoke.invokeService(this.Url,param,"emaiJobHander");
        XxlJobLogger.log("EmaiJobHandler End");
        return ret;
    }
}
