package com.shopmall.dry.shopmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shopmall.dry.shopmall.MyApplication;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.activity.LoginActivity;
import com.shopmall.dry.shopmall.activity.OrderActivity;
import com.shopmall.dry.shopmall.bean.User;
import com.shopmall.dry.shopmall.widget.Contants;
import com.squareup.picasso.Picasso;
//import android.app.Fragment;

/**
 * Created by DrY on 2017/6/20.
 */

public class MineFragment extends BaseFragment  {

    @ViewInject(R.id.img_head)
    private RoundedImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @ViewInject(R.id.btn_logout)
    private Button mbtnLogout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine,container,false);
    }

    @Override
    public void init() {
        User user =  MyApplication.getInstance().getUser();
        showUser(user);
    }


    @OnClick(R.id.txt_my_orders)
    public void toMyOrderActivity(View view){

        startActivity(new Intent(getActivity(), OrderActivity.class));
    }


    @OnClick(value = {R.id.img_head,R.id.txt_username})
    public void toLoginActivity(View view){

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        User user =  MyApplication.getInstance().getUser();
        showUser(user);
    }

    private void showUser(User user) {

        if( user != null){
            mTxtUserName.setText(user.getUsername());
            Picasso.with(getActivity()).load(user.getLogo_url()).into(mImageHead);
        }else {
            //mTxtUserName.setText(R.string.login);
        }

    }

}
