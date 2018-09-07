package com.judysen.remoteexecutor.core;

import okhttp3.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {
    private static OkHttpUtils okHttpUtils=new OkHttpUtils();
    public static OkHttpUtils init(){
        return okHttpUtils;
    }
    private OkHttpUtils(){
        this.client.dispatcher().setMaxRequestsPerHost(50);
        this.client.dispatcher().setMaxRequests(1000);
    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client=new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120,TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .build();

    /**
     *
     * @param param
     * @return
     */
    public String Post(String url,String param) throws IOException{
        RequestBody body = RequestBody.create(JSON, param);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    /**
     * From 提交
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public String postByFrom(String url,Map<String, String> map) throws IOException{
        okhttp3.FormBody.Builder formBodyBuilder = new okhttp3.FormBody.Builder();
        Iterator entries = map.entrySet().iterator();

        while(entries.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)entries.next();
            formBodyBuilder.add((String)entry.getKey(), (String)entry.getValue());
        }

        Request request = (new Request.Builder()).url(url).post(formBodyBuilder.build()).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
