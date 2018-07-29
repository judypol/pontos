package com.judysen.remoteexecutor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.judysen.remoteexecutor.core.OkHttpUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteExecutorApplicationTests {

	@Test
	public void contextLoads() {
	}
	@Test
	public void OkHttpUtils() throws IOException{
		String param="{\"json\":{\"MethodName\":\"generateContract\",\"ServiceName\":\"cmltp.fadada.service.IFadadaService\",\"Params\":\"{\\\"orderCode\\\":\\\"DL180628141901334931\\\"}\",\"authkeyid\":\"t_testuse\"}} ";
		JSONObject jsonObject= JSON.parseObject(param);
		jsonObject.getJSONObject("json").put("authkeyid","mltpJobHander");
		System.out.println(jsonObject.toJSONString());
		//String ret= OkHttpUtils.init().Post("http://192.168.68.50:5412/shcem",param);
		//System.out.println(ret);
	}
}
