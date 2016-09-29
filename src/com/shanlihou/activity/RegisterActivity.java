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
import com.shanlihou.tmp.UrlOpener;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by shily on 16-4-6.
 */
public class RegisterActivity extends TaoBaseActivity{
    private MyButton mBtRegist;
    private Handler mHandler;
    private Runnable mRegistRun;
    private TextView mTxSchool;
    private TextView mTxBuildings;
    private EditText mEtCellPhone;
    private EditText mEtRoom;
    private EditText mEtPasswd;
    private int mSchoolIndex;
    private String mStrName;
    private String mStrPasswd;
    private String mStrTelNum;
    private String mStrRoomNum;
    private String mStrBuildingNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtRegist = (MyButton) findViewById(R.id.bt_register);
        mBtRegist.setSpacing(15);
        mBtRegist.setText(getResources().getText(R.string.regist));
        mTxSchool = (TextView)findViewById(R.id.tx_school_choice);
        mTxBuildings = (TextView)findViewById(R.id.tx_building_choice);
        mEtCellPhone = (EditText)findViewById(R.id.et_cellphone);
        mEtPasswd = (EditText)findViewById(R.id.et_password);
        mEtRoom = (EditText)findViewById(R.id.et_room);
        mSchoolIndex = -1;
        init();
    }
    void init(){
        initHandler();
        initRun();
        initView();
    }

    @Override
    public int setContentViewId() {
        return R.layout.register;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (data != null){
                    Bundle bundle = data.getExtras();
                    if (bundle != null){
                        String name = bundle.getString("name");
                        int index = bundle.getInt("index");
                        if (index != mSchoolIndex){
                            mTxBuildings.setText(getResources().getText(R.string.building_num));
                            mTxBuildings.setTextColor(getResources().getColor(R.color.text_hint));
                        }
                        mSchoolIndex = index;
                        Log.d("shanlihou", "result is:" + name + ":" + index);
                        mTxSchool.setText(name);
                        mTxSchool.setTextColor(getResources().getColor(R.color.common_text));
                    }
                }
                break;
            case 1:
                if (data != null){
                    Bundle bundle = data.getExtras();
                    if (bundle != null){
                        String buildingNum = bundle.getString("buildingNum");
                        mTxBuildings.setText(buildingNum);
                        mTxBuildings.setTextColor(getResources().getColor(R.color.common_text));
                    }
                }
                break;
            default:
                break;
        }
    }

    protected void initHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case 0:
                        int status = (int)msg.obj;
                        if (status == 1){
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            doFinish();
                        }else{
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void doFinish() {
        RegisterActivity.this.finish();
    }

    protected void initView(){
        mBtRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击注册事件
                mStrTelNum = mEtCellPhone.getText().toString();
                mStrPasswd = mEtPasswd.getText().toString();
                mStrRoomNum = mEtRoom.getText().toString();
                mStrBuildingNum = mTxBuildings.getText().toString();
                new Thread(mRegistRun).start();
            }
        });
        mTxSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, SchoolChoiceActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        mTxBuildings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSchoolIndex == -1){
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, BuildingChoiceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("index", mSchoolIndex + "");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void initRun(){
        mRegistRun = new Runnable() {
            @Override
            public void run() {
                String url = MainApplication.URL_BASE;
                url += "/RegistBuyerServlet";
                String data = "password=" + mStrPasswd;
                data += "&school=" + mSchoolIndex;
                data += "&building_num=" + mStrBuildingNum;
                data += "&tel_num=" + mStrTelNum;
                data += "&room_num=" + mStrRoomNum;
                HttpContent req = UrlOpener.getInstance().urlPost(url, null, data);
                if (req != null){
                    Log.d("shanlihou", req.getContent());
                    JSONTokener jsonTokener = new JSONTokener(req.getContent());
                    try{
                        JSONObject jsonRet = (JSONObject)jsonTokener.nextValue();
                        int status = jsonRet.getJSONObject("response").getInt("status");
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
}
