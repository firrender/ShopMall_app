package com.shopmall.dry.shopmall.http;

import android.content.Context;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import dmax.dialog.SpotsDialog;

/**
 * Created by DrY on 2017/7/20.
 */

public abstract class SpotsCallBack<T> extends BaseCallback<T> {

    SpotsDialog dialog;

    public SpotsCallBack(Context context) {
        dialog = new SpotsDialog(context);
    }

    public void showDialog(){
        dialog.show();
    }

    public void dismissDialog(){
        if(dialog != null){
            dialog.dismiss();
        }
    }

    public void setMessage(String message){
        dialog.setMessage(message);
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();
    }

    @Override
    public void onSuccess(Response response, T t) {

    }

    @Override
    public void onError(Response response, int code, Exception e) {

    }

    @Override
    public void onResponse(Response request) {
        dismissDialog();
    }
}
