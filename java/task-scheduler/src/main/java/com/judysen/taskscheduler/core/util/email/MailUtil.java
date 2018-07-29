package com.judysen.taskscheduler.core.util.email;

import com.alibaba.fastjson.JSONObject;
import com.judysen.taskscheduler.config.TaskSchudulerConfiguration;
import com.judysen.taskscheduler.core.util.OkHttpUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class MailUtil {
    private static Logger logger = LoggerFactory.getLogger(com.judysen.taskscheduler.core.util.email.MailUtil.class);

    /**
     *
     * @param toAddress		收件人邮箱
     * @param mailSubject	邮件主题
     * @param mailBody		邮件正文
     * @return
     */
    public static boolean sendMail(String toAddress, String mailSubject, String mailBody){

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Recipient", toAddress);
            jsonObject.put("Sender", TaskSchudulerConfiguration.getAdminConfig().getMailUsername());
            jsonObject.put("Body", mailBody);
            jsonObject.put("IsHtmlBody", true);
            jsonObject.put("Subject", mailSubject);

            //jsonObject.put("cc","");          //带cc

            Map<String, String> data = new HashMap<>();
            data.put("sendBody", jsonObject.toJSONString());
            String res=OkHttpUtils.init().postByFrom(TaskSchudulerConfiguration.getAdminConfig().getMailUrl(),data);
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
}
