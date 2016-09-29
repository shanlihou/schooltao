package com.shanlihou.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.shanlihou.schooltao.R;

/**
 * Created by shanlihou on 2016/8/23.
 */
public abstract class TaoBaseActivity extends Activity implements OnBaseListener{
    protected ImageView mBtReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentViewId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mBtReturn = (ImageView)findViewById(R.id.btn_return);
        initiazed();
    }

    @Override
    public abstract int setContentViewId();


    @Override
    public void applyTheme() {
    }

    @Override
    public void initiazed() {
        if (mBtReturn != null){
            mBtReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doFinish();
                }
            });
        }
    }
    protected abstract void doFinish();
}
