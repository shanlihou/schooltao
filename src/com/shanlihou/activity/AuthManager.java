package com.shanlihou.activity;

import android.util.Log;

/**
 * Created by shily on 16-4-6.
 */
public class AuthManager {
    private static final String URL = "http://192.168.0.104:8080";
    private AuthManager(){
    }
    private static AuthManager mInstance = null;
    public static AuthManager getmInstance() {
        if (mInstance == null){
            mInstance = new AuthManager();
        }
        return mInstance;
    }

    public String getPubKey(){
        String url = URL + "/" + "Authentication?type=getPub";
        Log.d("shanlihou", "will get");
        HttpContent req = UrlOpener.getInstance().urlOpen(url, null);
        Log.d("shanlihou", "end get");
        return req.getContent();
    }
}
