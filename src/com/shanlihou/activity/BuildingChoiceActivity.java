package com.shanlihou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.shanlihou.schooltao.MainApplication;
import com.shanlihou.schooltao.R;
import com.shanlihou.tmp.HttpContent;
import com.shanlihou.tmp.UrlOpener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shanlihou on 2016/8/22.
 */
public class BuildingChoiceActivity extends Activity{
    Runnable mGetBuildingsRun;
    String mSchoolIndex;
    static final String URL_GET_BUILDINGS = "/BuildingOpenedListServlet?school_id=";
    Handler mHandler;
    List<Map<String, String>> mBuildingsList;
    ListView mBuildingListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_list);
        Bundle bundle = getIntent().getExtras();
        mSchoolIndex = bundle.getString("index");
        mBuildingListView = (ListView)findViewById(R.id.building_list);
        init();
        new Thread(mGetBuildingsRun).start();
    }
    void init(){
        initHandler();
        initRun();
        initView();
    }

    void initView(){
        mBuildingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String buildingNum = ((TextView)view.findViewById(R.id.building_item_text)).getText().toString();
                Intent intent = new Intent();
                intent.putExtra("buildingNum", buildingNum);
                BuildingChoiceActivity.this.setResult(1, intent);
                BuildingChoiceActivity.this.finish();
            }
        });
    }
    void initHandler(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        setList();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    void initRun(){
        mGetBuildingsRun = new Runnable() {
            @Override
            public void run() {
                String url = MainApplication.URL_BASE + URL_GET_BUILDINGS + mSchoolIndex;
                HttpContent req = UrlOpener.getInstance().urlOpen(url, null);
                Log.d("shanlihou", "building:" + req.getContent());
                mBuildingsList = getBuildingsList(req.getContent());
                mHandler.sendEmptyMessage(0);
            }
        };
    }
    void setList(){
        mBuildingListView.setAdapter(new SimpleAdapter(this, mBuildingsList, R.layout.building_item, new String[]{"buildingNum"}, new int[]{R.id.building_item_text}));
    }
    List<Map<String, String>> getBuildingsList(String jsonStr){
        List<Map<String, String>> ret = new ArrayList<>();
        JSONTokener jsonTokener = new JSONTokener(jsonStr);
        JSONObject jsonRet = null;
        try{
            jsonRet = (JSONObject)jsonTokener.nextValue();
            if (jsonRet.has("response") && jsonRet.getJSONObject("response").has("building")){
                JSONArray jsonBuildings = jsonRet.getJSONObject("response").getJSONArray("building");
                int len = jsonBuildings.length();
                for (int i = 0; i < len; i++){
                    JSONObject jsonBuilding = jsonBuildings.getJSONObject(i);
                    Map<String, String> map = new HashMap<>();
                    map.put("buildingNum", jsonBuilding.getString("buildingOpened"));
                    ret.add(map);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
}
