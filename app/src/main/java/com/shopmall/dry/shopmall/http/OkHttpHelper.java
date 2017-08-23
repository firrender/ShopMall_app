package com.shopmall.dry.shopmall.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by DrY on 2017/7/20.
 */

public class OkHttpHelper {

    private Gson gson;
    private Handler mHandler;
    private static OkHttpClient okHttpClient;

    //初始化
    private OkHttpHelper(){
        gson = new Gson();
        okHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
    };

    public static OkHttpHelper getInstance(){
        return new OkHttpHelper();
    }

    public void get(String url,BaseCallback callback){
        Request request = buildRequest(url,null,HttpMehodType.GET);
        doRequest(request,callback);
    }

    public void post(String url, Map<String,String> params,BaseCallback callback){
        Request request = buildRequest(url,params,HttpMehodType.GET);
        doRequest(request,callback);
    }


    public void doRequest(Request request, final BaseCallback baseCallback){
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                baseCallback.onFailure(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {

                baseCallback.onResponse(response);

                if(response.isSuccessful()){
                    String resultStr = response.body().string();
                    if(baseCallback.type == String.class){
                        //baseCallback.onSuccess(response,resultStr);
                        callbackSuccess(baseCallback,response,resultStr);
                    }else {

                        try {
                            Object object = gson.fromJson(resultStr, baseCallback.type);
                            //baseCallback.onSuccess(response, null);
                            callbackSuccess(baseCallback,response,object);
                        } catch (JsonSyntaxException e) {
                            //e.printStackTrace();
                            baseCallback.onError(response,response.code(),e);
                        }

                    }

                }else {
                    //baseCallback.onError(response,response.code(),null);
                    callbackError(baseCallback,response,null);
                }
               // gson.fromJson(response.body().string(),baseCallback.type);
            }
        });
    }

    private void callbackSuccess(final  BaseCallback baseCallback , final Response response, final Object obj ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                baseCallback.onSuccess(response, obj);
            }
        });
    }


    private void callbackError(final  BaseCallback baseCallback , final Response response, final Exception e ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                baseCallback.onError(response,response.code(),e);
            }
        });
    }

    private Request buildRequest(String url,Map<String,String> params,HttpMehodType mehodType){

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if(mehodType == HttpMehodType.GET){
            builder.get();
        }else if(mehodType == HttpMehodType.POST){
            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        return builder.build();
    }

    private RequestBody buildFormData(Map<String,String> params){
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if(params != null){
            for(Map.Entry<String,String> entry : params.entrySet()){
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        return builder.build();
    }

    enum HttpMehodType{
        GET,
        POST,
    }

}
