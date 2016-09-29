package com.shanlihou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.shanlihou.adapter.SchoolListAdapter;
import com.shanlihou.schooltao.R;
import com.shanlihou.tmp.SchoolManager;

import java.util.List;
import java.util.Map;


/**
 * Created by shanlihou on 2016/6/19.
 */
public class SchoolChoiceActivity extends Activity{
    Context mContext;
    ListView mSchoolListView;
    SchoolListAdapter mListAdapter;
    Runnable mGetSchoolListRun;
    Handler mHandler;
    List<Map> mSchoolList;
    int mScrolly = 0;
    int mScrollx = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_list);
        mSchoolListView = (ListView)findViewById(R.id.school_list);
        mContext = this;
        /*
        List<Map> list = new ArrayList<>();
        mMap = parseJson(text);
        Log.d("shanlihou", "map is:" + mMap.toString());
        getList(mMap, list, 0);
        mListAdapter = new SchoolListAdapter(this, list);
        mSchoolListView.setAdapter(mListAdapter);*/
        init();
    }
    void init(){
        handleInit();
        viewInit();
        runInit();
        new Thread(mGetSchoolListRun).start();
    }
    void setList(){
        mSchoolList = SchoolManager.getInstance().getList();
        mListAdapter = new SchoolListAdapter(mContext, mSchoolList);
        mSchoolListView.setAdapter(mListAdapter);
    }
    void handleInit(){
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
    void runInit()
    {
        mGetSchoolListRun = new Runnable() {
            @Override
            public void run() {
                if (!SchoolManager.getInstance().getMap()){
                    return;
                }
                mHandler.sendEmptyMessage(0);
            }
        };
    }
    void viewInit(){
        mSchoolListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolListAdapter.ViewHolder holder = (SchoolListAdapter.ViewHolder) view.getTag();
                if (holder.map != null){
                    if (holder.map.get("close") == null){
                        holder.map.put("close", 1);
                    }else{
                        holder.map.put("close", null);
                    }
                    setList();
                    mSchoolListView.setSelectionFromTop(mScrollx, mScrolly);
                }else if(holder.index != -1){
                    Intent intent = new Intent();
                    TextView textView = (TextView)view.findViewById(R.id.school_item_text);
                    intent.putExtra("name", textView.getText().toString());
                    intent.putExtra("index", holder.index);
                    SchoolChoiceActivity.this.setResult(0, intent);
                    SchoolChoiceActivity.this.finish();
                }
            }
        });
        mSchoolListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // scrollPos记录当前可见的List顶端的一行的位置
                    mScrollx = mSchoolListView.getFirstVisiblePosition();
                }
                if (mSchoolList != null) {
                    View v = mSchoolListView .getChildAt(0);
                    mScrolly = (v == null) ? 0 : v.getTop();
                }
                Log.d("shanlihou", "x:" + mScrollx + ",y:" + mScrolly);
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }
}
