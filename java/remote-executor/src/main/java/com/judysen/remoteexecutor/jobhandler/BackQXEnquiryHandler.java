package com.judysen.remoteexecutor.jobhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judysen.remoteexecutor.core.OkHttpUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@JobHandler("backQXEnquiryHandler")
public class BackQXEnquiryHandler extends IJobHandler{
    @Value("${shcem.middle.url}")
    private String proxyIp;
    @Value("${mail.url}")
    private String mailUrl;
    @Value("${mode}")
    private String mode;
    /**
     * execute handler, invoked when executor receives a scheduling request
     *
     * @param param
     * @return
     * @throws Exception
     */
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("backQXEnquiry Job is Start,the Param is "+param);
        try{
            this.run(param);
            XxlJobLogger.log("backQXEnquiry Job is successful,");
            return ReturnT.SUCCESS;
        }catch (Exception ex){
            XxlJobLogger.log("backQXEnquiry Job is Fail,the err message is ",ex);
            return new ReturnT<>(500,ex.getMessage());
        }
    }
    /**
     * 执行自动Job
     */
    public void run(String arg0) throws Exception {

        // 取得执行参数
        JSONObject data = JSON.parseObject(arg0);
        String serviceName = data.getString("servicename");
        String methodName = data.getString("methodname");
        int effectTime = data.getIntValue("effectTime");

        String mode = this.mode;
        StringBuilder sb = new StringBuilder();
        StringBuilder sbLead = new StringBuilder();
        String GUID = UUID.randomUUID().toString();
        String message = this.getClass().getName();
        sb.append("-----------------" + message + "开始-----------------。</br>");
        sb.append("GUID:" + GUID + "</br>");
        sb.append("本次执行时间:" );
        sbLead.append(sb.toString());
        JSONObject js = getJSONObject(serviceName, methodName, effectTime);
        JSONObject jsLead = getJSONObject(serviceName, "backQXLeads", effectTime);
        sb.append("jsEnquiry:" + js.toString());
        sbLead.append("jsLead:" + jsLead.toString());

        XxlJobLogger.log(mode, sb.toString());
        XxlJobLogger.log(mode, sbLead.toString());

        String errMsg = "";
        String errMsgLeads = "";
        /**
         * 取得数据 Start
         */
        String response= OkHttpUtils.init().Post(this.proxyIp,js.toJSONString());

        JSONObject jsoSource = null;
        jsoSource = JSON.parseObject(response);

        /**
         * 取得数据 End
         */

        String rtnCode = "";
        if (jsoSource != null && jsoSource.containsKey("CODE")) {
            rtnCode = jsoSource.getString("CODE");
        } else {
            errMsg = message + "的Job(线性商城撤销询盘任务)执行发生错误，返回数据不正确：" + jsoSource.toString();
            XxlJobLogger.log(mode, errMsg);
            throw new Exception(errMsg);
        }
        // 判断结果是否正常
        if (!rtnCode.endsWith("00000")) {
            errMsg = message + "的Job(线性商城撤销询盘任务)执行发生错误，返回错误结果：" + jsoSource.toString();
            XxlJobLogger.log(mode, errMsg);
            throw new Exception(errMsg);
        } else {
            XxlJobLogger.log(mode, message + "的Job(线性商城撤销询盘任务)执行成功");
        }

        // 撤销询盘执行成功，继续撤销报盘
        /**
         * 取得数据 Start
         */
        response = OkHttpUtils.init().Post(this.proxyIp,jsLead.toJSONString());

        jsoSource = JSON.parseObject(response);

        /**
         * 取得数据 End
         */

        rtnCode = jsoSource.getString("CODE");
        // 判断结果是否正常
        if (!rtnCode.endsWith("00000")) {
            errMsgLeads = message + "的Job(线性商城撤销挂盘任务)执行发生错误，返回错误结果：" + jsoSource.toString();
            XxlJobLogger.log(mode, errMsgLeads);
            throw new Exception(errMsg);
        } else {
            XxlJobLogger.log(mode, message + "的Job(线性商城撤销挂盘任务)执行成功");
        }

        XxlJobLogger.log(mode, "-----------------" + message + "结束-----------------。</br>" + "GUID:" + GUID + "</br>" + " 下次执行时间:"
                );
    }

    private JSONObject getJSONObject(String serviceName, String methodName, int effectTime) {

        // 执行服务
        Map<String, String> service = new HashMap<String, String>();
        service.put("ServiceName", serviceName);
        service.put("MethodName", methodName);
        JSONObject jsParam = new JSONObject();
        jsParam.put("differ", 2);
        jsParam.put("effectTime", effectTime);
        service.put("Params", jsParam.toString());
        service.put("authkeyid", "t_autoTaskJob");

        JSONObject js = new JSONObject();
        js.put("json", service);

        return js;
    }
}
