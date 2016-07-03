package com.shanlihou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import com.shanlihou.schooltao.R;
import com.shanlihou.tmp.AuthManager;
import com.shanlihou.tmp.MyButton;

/**
 * Created by shily on 16-4-6.
 */
public class RegisterActivity extends Activity{
    private MyButton mBtRegist;
    private Handler mHandler;
    private Runnable mRegistRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mBtRegist = (MyButton) findViewById(R.id.bt_register);
        mBtRegist.setSpacing(15);
        mBtRegist.setText(getResources().getText(R.string.regist));
        init();
    }
    private void init(){
        initHandler();
        initRun();
        initView();
    }
    private void initHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){

                    default:
                        break;
                }
            }
        };
    }
    private void initView(){
        mBtRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击注册事件
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initRun(){
        mRegistRun = new Runnable() {
            @Override
            public void run() {
                Log.d("shanlihou", "get pub");
                String pub = AuthManager.getmInstance().getPubKey();
                Log.d("shanlihou", "end pub");
                Log.d("shanlihou", "pub is :" + pub);
            }
        };
    }
}
