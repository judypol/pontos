package com.judysen.remoteexecutor.jobhandler;

import com.alibaba.fastjson.JSON;
import com.judysen.remoteexecutor.core.RemoteServiceInvoke;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@JobHandler(value="mltpJobHandler")
@Component
public class MLTPJobHandler extends IJobHandler {

    @Value("${mltp.middle.url}")
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
        XxlJobLogger.log("MLTPJobHandler Start, the Param is "+param+"\nURL is "+this.Url);
        ReturnT ret= RemoteServiceInvoke.invokeService(this.Url,param,"emaiJobHander");
        XxlJobLogger.log(JSON.toJSONString(ret));
        XxlJobLogger.log("MLTPJobHandler End");
        return ret;
    }
}
