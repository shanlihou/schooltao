package com.shanlihou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;
import com.shanlihou.schooltao.MainApplication;
import com.shanlihou.schooltao.R;
import com.shanlihou.tmp.SchoolManager;

/**
 * Created by shanlihou on 2016/8/9.
 */
public class HomeActivity extends Activity {
    ListView mShopList;
    TextView mTxSchool;
    Runnable mRunGetSchoolName;
    Handler mHandler;
    String mStrSchoolName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mShopList = (ListView)findViewById(R.id.shop_list);
        mTxSchool = (TextView)findViewById(R.id.tx_school);
        init();
        new Thread(mRunGetSchoolName).start();
    }
    void init(){
        initHandler();
        initRun();
    }
    void initHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        mTxSchool.setText(mStrSchoolName);
                        break;
                    default:
                        break;
                }
            }
        };
    }
    void initRun(){
        mRunGetSchoolName = new Runnable() {
            @Override
            public void run() {
                mStrSchoolName = MainApplication.getInstance().getShared("schoolName");
                if (mStrSchoolName != null){
                    mHandler.sendEmptyMessage(0);
                }
                String strId = MainApplication.getInstance().getShared("schoolId");
                if (strId == null){
                    return;
                }
                int schoolId = Integer.parseInt(strId);
                mStrSchoolName = SchoolManager.getInstance().getSchoolName(schoolId);
                if (mStrSchoolName == null){
                    return;
                }
                MainApplication.getInstance().setShared("schoolName", mStrSchoolName);
                mHandler.sendEmptyMessage(0);
            }
        };
    }
}
