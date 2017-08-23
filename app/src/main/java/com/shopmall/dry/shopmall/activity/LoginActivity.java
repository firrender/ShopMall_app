package com.shopmall.dry.shopmall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shopmall.dry.shopmall.MyApplication;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.bean.User;
import com.shopmall.dry.shopmall.http.OkHttpHelper;
import com.shopmall.dry.shopmall.http.SpotsCallBack;
import com.shopmall.dry.shopmall.msg.LoginRespMsg;
import com.shopmall.dry.shopmall.utils.DESUtil;
import com.shopmall.dry.shopmall.utils.ToastUtils;
import com.shopmall.dry.shopmall.widget.ClearEditText;
import com.shopmall.dry.shopmall.widget.Contants;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity implements View.OnClickListener {




    @ViewInject(R.id.etxt_phone)
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.etxt_pwd)
    private ClearEditText mEtxtPwd;
    @ViewInject(R.id.btn_login)
    private Button mButtonLogin;
    @ViewInject(R.id.txt_toReg)
    private TextView mNewtoreg;
    @ViewInject(R.id.toolbar_back)
    private ImageButton mToolbarBack;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        mButtonLogin.setOnClickListener(this);
        mNewtoreg.setOnClickListener(this);
        mToolbarBack.setOnClickListener(this);
        //initToolBar();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String phone = mEtxtPhone.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    ToastUtils.show(this, "请输入手机号码");
                    return;
                }

                String pwd = mEtxtPwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)){
                    ToastUtils.show(this,"请输入密码");
                    return;
                }

                Map<String,String> params = new HashMap<>(2);
                params.put("phone",phone);
                params.put("password", DESUtil.encode(Contants.DES_KEY,pwd));
                okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallBack<LoginRespMsg<User>>(this) {

                    @Override
                    public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                        MyApplication myApplication = MyApplication.getInstance();
                        MyApplication.getInstance().putUser(userLoginRespMsg.getData(),userLoginRespMsg.getToken());
                        if(myApplication != null){
                            setResult(RESULT_OK);
                            finish();
                        }else {
                            myApplication.jumpToTargetActivity(LoginActivity.this);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });

                break;

            case R.id.txt_toReg:
                Intent intent = new Intent(this, RegActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                break;

            case R.id.toolbar_back:
                finish();
                break;
        }
    }
}
