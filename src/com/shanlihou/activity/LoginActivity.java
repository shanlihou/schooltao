package com.shanlihou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import com.shanlihou.schooltao.R;

/**
 * Created by Administrator on 2016/3/21 0021.
 */
public class LoginActivity extends Activity{
    private MyButton mBtLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mBtLogin = (MyButton)findViewById(R.id.bt_login);
        mBtLogin.setSpacing(15);
        mBtLogin.setText(getResources().getText(R.string.log_in));
    }
}
