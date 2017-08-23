package com.shopmall.dry.shopmall.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import dmax.dialog.SpotsDialog;

/**
 * Created by DrY on 2017/7/28.
 */

public class RegActivity extends BaseActivity {
    private static final String TAG = "RegActivity";

    @ViewInject(R.id.toolbar_back)
    private ImageButton mToolbarBack;
    @ViewInject(R.id.txtCountry)
    private TextView mTxtCountry;
    @ViewInject(R.id.txtCountryCode)
    private TextView mTxtCountryCode;
    @ViewInject(R.id.edit_phone)
    private ClearEditText mEditPhone;
    @ViewInject(R.id.edit_pwd)
    private ClearEditText mEditPwd;
    @ViewInject(R.id.edit_code)
    private ClearEditText mEditCode;
    @ViewInject(R.id.button_code)
    private Button mBtCode;
    @ViewInject(R.id.regist)
    private Button mRegist;

    private int TIME = 60;//倒计时60s
    private static final int CODE_ING = 1;   //已发送，倒计时
    private static final int CODE_REPEAT = 2;  //重新发送
    private static final int SMSDDK_HANDLER = 3;  //短信回调
    private EventHandler  eventHandler;
    private SpotsDialog dialog;
    private OkHttpHelper okHttpHelper  = OkHttpHelper.getInstance();
    private String userPhone;
    private String userPwd;
    private String code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        ViewUtils.inject(this);

        code = mTxtCountryCode.getText().toString().trim();
        userPwd = mEditPwd.getText().toString().trim();
        initView();//按钮初始化

        SMSSDK.initSDK(getApplicationContext(), "1fbf77efa148c", "ff54adb890d0ceeb758ced51133112f2");
        initSMS();
        /*eventHandler = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(eventHandler);*/

        dialog = new SpotsDialog(this);


    }

    private void initSMS(){
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = SMSDDK_HANDLER;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void initView(){

        mBtCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone = mEditPhone.getText().toString().trim().replaceAll("\\s*", "");
                String rule = "^1(3|5|7|8|4)\\d{9}";
                Pattern p = Pattern.compile(rule);
                Matcher m = p.matcher(userPhone);
                if (TextUtils.isEmpty(userPhone)) {

                    ToastUtils.show(RegActivity.this, "请输入手机号码");
                    return;

                }else if(userPhone.length() != 11) {

                        ToastUtils.show(RegActivity.this,"手机号码长度不对");
                        return;

                }else if (!m.matches()) {

                    ToastUtils.show(RegActivity.this,"您输入的手机号码格式不正确");
                    return;

                }else {

                    new AlertDialog.Builder(RegActivity.this)
                            .setTitle("发送短信")
                            .setMessage("我们将把验证码发送到以下号码:\n" + "+86:" + userPhone)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SMSSDK.getVerificationCode(code, userPhone);
                                    mBtCode.setClickable(false);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 60; i > 0; i--) {
                                                handler.sendEmptyMessage(CODE_ING);
                                                if (i <= 0) {
                                                    break;
                                                }
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            handler.sendEmptyMessage(CODE_REPEAT);
                                        }
                                    }).start();
                                }
                            })
                    .create().show();
                }
            }
        });
        mRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对验证码进行验证->回调函数
                //SMSSDK.submitVerificationCode(code, userPhone, mEditCode.getText().toString());
                submitCode();
            }
        });
        mToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private  void submitCode(){

        String vCode = mEditCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(RegActivity.this, R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(code,userPhone,vCode);
        dialog.show();
    }

    Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case CODE_ING://已发送,倒计时
                    mBtCode.setText("重新发送("+--TIME+"s)");
                    break;
                case CODE_REPEAT://重新发送
                    mBtCode.setText("获取验证码");
                    mBtCode.setClickable(true);
                    break;
                case SMSDDK_HANDLER:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if(dialog !=null && dialog.isShowing())
                        dialog.dismiss();
                    //回调完成
                    if (result == SMSSDK.RESULT_COMPLETE)
                    {
                        //验证码验证成功
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                        {
                            //Toast.makeText(RegActivity.this, "验证成功", Toast.LENGTH_LONG).show();
                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();
                            if(dialog !=null && dialog.isShowing())
                                dialog.dismiss();
                            startActivity(new Intent(RegActivity.this,MainActivity.class));
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mEditCode.getWindowToken(), 0) ;
                            finish();

                        }
                        //已发送验证码
                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE)
                        {
                            Toast.makeText(RegActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    if(result==SMSSDK.RESULT_ERROR)
                    {
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(RegActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            //do something
                        }
                    }
                    break;
                /*case R.id.register_status:
                    String result_code = msg.getData().getString("result").toString();
                    if("1".equals(result_code))
                    {
                        Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        //intent.putExtra("userPhone", userPhone);
                        //MainActivity.this.setResult(RESULE_CODE, intent);
                        //startActivity(intent);
                        finish();
                    }else
                    {
                        Toast.makeText(MainActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                    }
                    break;*/
                /*case R.id.check_phone_exist://手机号是否已存在
                    String result_code_2 = msg.getData().getString("result").toString();
                    if("1".equals(result_code_2))
                    {
                        errPhoneText.setText("手机号码已经注册，请换用其他号码");
                        resultMap.put("phone", false);
                    }
                    else
                    {
                        errPhoneText.setText("");
                        resultMap.put("phone", true);
                    }
                    break;*/
            }
        }
    };

    private void doReg(){

        Map<String,String> params = new HashMap<>(2);
        params.put("phone",userPhone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, userPwd));

        okHttpHelper.post(Contants.API.REG, params, new SpotsCallBack<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                if(dialog !=null && dialog.isShowing())
                    dialog.dismiss();

                if(userLoginRespMsg.getStatus()== LoginRespMsg.STATUS_ERROR){
                    ToastUtils.show(RegActivity.this,"注册失败:"+userLoginRespMsg.getMessage());
                    return;
                }
                MyApplication application =MyApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                startActivity(new Intent(RegActivity.this,MainActivity.class));
                finish();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }


        });
    }

    /*private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        // 解析国家列表
        for (HashMap<String, Object> country : countries) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }
            Log.d(TAG,"code="+code + "rule="+rule);
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
