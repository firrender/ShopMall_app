package com.shopmall.dry.shopmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.shopmall.dry.shopmall.activity.LoginActivity;
import com.shopmall.dry.shopmall.MyApplication;
import com.shopmall.dry.shopmall.bean.User;

/**
 * Created by DrY on 2017/7/27.
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView(inflater,container,savedInstanceState);
        ViewUtils.inject(this,view);
        init();
        initToolBar();
        return view;
    }

    protected  void initToolBar(){}

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void init();

    //跳转activity是否要登入
    public void startActivity(Intent intent, boolean isNeedLogin){

        if(isNeedLogin){
            User user = MyApplication.getInstance().getUser();
            if(user !=null){
                super.startActivity(intent);
            } else{
                MyApplication.getInstance().putIntent(intent);
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                super.startActivity(loginIntent);
            }
        } else{
            super.startActivity(intent);
        }
    }
}
