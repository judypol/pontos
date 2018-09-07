package com.judysen.remoteexecutor.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judysen.remoteexecutor.jobhandler.Constants;
import com.xxl.job.core.log.XxlJobLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MLTPUtils {
    public static JSONObject httpPost(String proxyIp, JSONObject js) throws IOException {
        XxlJobLogger.log("服务执行： " + js.toString());

        String response=OkHttpUtils.init().Post(proxyIp,js.toJSONString());
        JSONObject jsoSource=null;
        if (response != null) {
            // 数据转换成JSON对象
            jsoSource = JSON.parseObject(response);
        }
        /**
         * 取得数据 End
         */

        return jsoSource;
    }

    public static JSONObject generataPostData(String serviceName, String methodName, String params) {

        Map<String, String> service = new HashMap<String, String>();

        service.put("ServiceName", serviceName);
        service.put("MethodName", methodName);
        if (params != null && !"none".equals(params)) {
            service.put("Params", params);
        }
        service.put("authkeyid", "t_autoTaskJob");
        JSONObject js = new JSONObject();
        js.put("json", service);

        return js;
    }

    public static boolean isSuccess(JSONObject jsoRtn) {

        boolean check = false;

        if (jsoRtn != null && jsoRtn.containsKey("CODE") && jsoRtn.getString("CODE").endsWith("0000")) {
            check = true;
        }

        return check;
    }

    /**
     *
     * @param jsoRtn
     * @return true: 交易交收日；false： 非交易交收日
     */
    public static boolean ifDeliveryday(JSONObject jsoRtn) {

        boolean check = false;

        if (jsoRtn != null && jsoRtn.getString("CODE").endsWith("0000")) {
            if (jsoRtn.containsKey("DATA")) {

                int differ = Constants.NON_DEAL_DAY;
                String data = jsoRtn.getString("DATA");
                JSONObject jsoData = JSON.parseObject(data);
                differ = jsoData.getIntValue("differ");

                // 2为交易交收日
                if (differ == Constants.DELIVERY_DAY) {
                    check = true;
                }
            }
        }

        return check;
    }
    /**
     *
     * @param jsoRtn
     * @return true: 交易日；false： 非交易日
     */
    public static boolean ifDealday(JSONObject jsoRtn) {

        boolean check = false;

        if (jsoRtn != null && jsoRtn.containsKey("CODE") && jsoRtn.getString("CODE").endsWith("0000")) {
            if (jsoRtn.containsKey("DATA")) {

                int differ = Constants.NON_DEAL_DAY;

                String data = jsoRtn.getString("DATA");
                JSONObject jsoData = JSON.parseObject(data);
                differ = jsoData.getIntValue("differ");

                // 1为交易日,2为交易交收日
                if (differ == Constants.DEAL_DAY || differ == Constants.DELIVERY_DAY) {
                    check = true;
                }
            }
        }

        return check;
    }

    /**
     *
     * @param jsoRtn
     */
    public static String getDeliveryContractToday(JSONObject jsoRtn) {

        String content = null;
        String data = jsoRtn.getString("DATA");
        JSONObject jsoData = JSON.parseObject(data);
        content = jsoData.getString("content");

        return content;
    }
}
