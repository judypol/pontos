package com.judysen.remoteexecutor.jobhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judysen.remoteexecutor.core.OkHttpUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@JobHandler("confirmInMoneyFlowHandler")
public class ConfirmInMoneyFlowHandler extends IJobHandler {
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
        XxlJobLogger.log(this.getClass().getName()+"start,the param is "+param);
        try{
            run(param);
            XxlJobLogger.log(this.getClass().getName()+" is success");
            return ReturnT.SUCCESS;
        }catch (Exception ex){
            XxlJobLogger.log(this.getClass().getName()+" is fail",ex);
            return new ReturnT<>(500,ex.toString());
        }
    }
    private void run(String param) throws Exception{
        // 取得执行参数
        JSONObject data = JSON.parseObject(param);
        String serviceName = data.getString("servicename");
        String methodName = data.getString("methodname");

        StringBuilder sb = new StringBuilder();

        sb.append("-----------------" + this.getClass().getName() + "开始-----------------。</br>");

        // 执行服务
        Map<String, String> service = new HashMap<String, String>();
        service.put("ServiceName", serviceName);
        service.put("MethodName", methodName);

        JSONObject obj = new JSONObject();
        obj.put("tDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        service.put("Params", obj.toString());

        service.put("authkeyid", "t_autoTaskJob");
        JSONObject js = new JSONObject();
        js.put("json", service);

        XxlJobLogger.log(mode, js.toJSONString());

        String errMsg = "";
        /**
         * 取得数据 Start
         */
        String response = OkHttpUtils.init().Post(this.proxyIp,js.toJSONString());

        JSONObject jsoSource = JSON.parseObject(response);
        /**
         * 取得数据 End
         */

        String rtnCode = "";
        if (jsoSource != null && jsoSource.containsKey("CODE")) {
            rtnCode = jsoSource.getString("CODE");
        } else {
            errMsg = "Job执行发生错误，返回数据不正确：" + jsoSource.toString();
            throw new Exception(errMsg);
        }
        // 判断结果是否正常
        if (!rtnCode.contains("00000")) {
            errMsg = "Job执行发生错误，返回错误结果：" + jsoSource.toString();
            throw new Exception(errMsg);
        }

        XxlJobLogger.log(mode, "-----------------" + "结束-----------------。</br>" + "GUID:"  + "</br>" + " 下次执行时间:"
                );
    }
}
