package com.shanlihou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.shanlihou.schooltao.MainApplication;
import com.shanlihou.schooltao.R;
import com.shanlihou.tmp.HttpContent;
import com.shanlihou.tmp.MyButton;
import com.shanlihou.tmp.SchoolManager;
import com.shanlihou.tmp.UrlOpener;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Administrator on 2016/3/21 0021.
 */
public class LoginActivity extends TaoBaseActivity{
    private MyButton mBtLogin;
    private TextView mTxRegist;
    private EditText mEtPhonenumber;
    private EditText mEtPasswd;
    private Runnable mLoginRun;
    private String mStrPhonenumber;
    private String mStrPasswd;
    private android.os.Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtLogin = (MyButton)findViewById(R.id.bt_login);
        mBtLogin.setSpacing(15);
        mBtLogin.setText(getResources().getText(R.string.log_in));
        mTxRegist = (TextView)findViewById(R.id.bt_register);
        mEtPhonenumber = (EditText)findViewById(R.id.et_cellphone);
        mEtPasswd = (EditText)findViewById(R.id.et_password);
        init();
        if (MainApplication.getInstance().getShared("token") != null){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public int setContentViewId() {
        return R.layout.login;
    }

    @Override
    protected void doFinish() {

    }

    void init(){
        initHandler();
        initRun();
        initView();
    }

    private void initView() {
        mTxRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStrPhonenumber = mEtPhonenumber.getText().toString();
                mStrPasswd = mEtPasswd.getText().toString();
                new Thread(mLoginRun).start();
            }
        });
    }
    private void initRun(){
        mLoginRun = new Runnable() {
            @Override
            public void run() {
                String url = MainApplication.URL_BASE + "/BuyersLoginServlet";
                String data = "tel_num=";
                data += mStrPhonenumber;
                data += "&paswd=";
                data += mStrPasswd;
                data += "&platform=android";
                HttpContent req = UrlOpener.getInstance().urlPost(url, null, data);
                if (req != null){
                    Log.d("shanlihou", "req:" + req.getContent());
                    JSONTokener jsonTokener = new JSONTokener(req.getContent());
                    try{
                        JSONObject jsonObject = (JSONObject)jsonTokener.nextValue();
                        JSONObject jsonResp = jsonObject.getJSONObject("response");
                        int status = jsonResp.getInt("status");
                        if (status == 1){
                            JSONObject jsonUser = jsonResp.getJSONObject("userInfo");
                            MainApplication.getInstance().setShared("userInfo", jsonResp.getString("userInfo"));
                            int schoolId = jsonUser.getInt("schoolId");
                            MainApplication.getInstance().setShared("schoolId", schoolId + "");
                            MainApplication.getInstance().setShared("token", jsonResp.getString("token"));
                            String schoolName = SchoolManager.getInstance().getSchoolName(schoolId);
                            if (schoolName != null){
                                MainApplication.getInstance().setShared("schoolName", schoolName);
                            }
                        }
                        Message message = new Message();
                        message.what = 0;
                        message.obj = status;
                        mHandler.sendMessage(message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
    }
    private void initHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 0:
                        int status = (int)msg.obj;
                        if (status == 1){
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
