package com.shopmall.dry.shopmall.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.shopmall.dry.shopmall.MyApplication;
import com.shopmall.dry.shopmall.bean.User;

/**
 *
 * Created by DrY on 2017/7/27.
 */

public class BaseActivity extends AppCompatActivity {
    //跳转activity是否要登入
    public void startActivity(Intent intent, boolean isNeedLogin){

        if(isNeedLogin){
            User user = MyApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            } else{
                MyApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(this, LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else{
            super.startActivity(intent);
        }
    }
}
