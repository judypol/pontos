package com.judysen.remoteexecutor.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judysen.remoteexecutor.core.models.ResModel;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.util.StringUtils;

import static com.xxl.job.core.handler.IJobHandler.FAIL;
import static com.xxl.job.core.handler.IJobHandler.SUCCESS;

public class RemoteServiceInvoke {
    public static ReturnT<String> invokeService(String url,String param,String authKeyId) throws Exception{
        if(StringUtils.isEmpty(param)){
            return FAIL;
        }
        JSONObject jsonObject= JSON.parseObject(param);
        jsonObject.getJSONObject("json").put("authkeyid",authKeyId);

        String ret= OkHttpUtils.init().Post(url,param);
        XxlJobLogger.log("执行结果："+ret);
        ResModel resModel= JSON.parseObject(ret,ResModel.class);
        if(resModel.getCode().equals("00000")||
                resModel.getCode().equals("MSG00000")||resModel.getCode().equals("INFO0000")){
            return SUCCESS;
        }else{
            return FAIL;
        }
    }
}
