package com.shanlihou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.shanlihou.schooltao.R;

public class MyActivity extends Activity {
    /**
     * Called when the com.shanlihou.activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = new Intent(MyActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}
