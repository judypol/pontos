package com.judysen.remoteexecutor.core;

import okhttp3.*;

import java.io.IOException;

public class OkHttpUtils {
    private static OkHttpUtils okHttpUtils=new OkHttpUtils();
    public static OkHttpUtils init(){
        return okHttpUtils;
    }
    private OkHttpUtils(){

    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client=new OkHttpClient();
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
}
