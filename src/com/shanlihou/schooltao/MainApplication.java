package com.shanlihou.schooltao;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Administrator on 2016/3/20 0020.
 */
public class MainApplication extends Application{
    public static final String URL_BASE = "http://120.26.109.211:8080/SuTaServer";
    private SharedPreferences mShared;
    private static MainApplication instance = null;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("shanlihou", "app create");
        mShared = getSharedPreferences("SchoolTao", Context.MODE_PRIVATE);
        instance = this;
    }
    public void setShared(String key, String value){
        SharedPreferences.Editor editor = mShared.edit();
        if (value == null){
            editor.remove(key);
        }
        else{
            editor.putString(key,value);
        }
        editor.commit();
    }
    public String getShared(String key){
        return mShared.getString(key, null);
    }
    public static MainApplication getInstance(){
        return instance;
    }
}
