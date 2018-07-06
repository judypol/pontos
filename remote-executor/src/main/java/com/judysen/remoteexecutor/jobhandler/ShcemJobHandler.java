package com.judysen.remoteexecutor.jobhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judysen.remoteexecutor.core.OkHttpUtils;
import com.judysen.remoteexecutor.core.RemoteServiceInvoke;
import com.judysen.remoteexecutor.core.models.ResModel;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@JobHandler(value="shcemJobHandler")
@Component
public class ShcemJobHandler extends IJobHandler {

    @Value("shcem.middle.url")
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
        XxlJobLogger.log("ShcemJobHandler Start, the Param is "+param);
        ReturnT ret= RemoteServiceInvoke.invokeService(this.Url,param,"emaiJobHander");
        XxlJobLogger.log("ShcemJobHandler End");
        return ret;
    }
}
