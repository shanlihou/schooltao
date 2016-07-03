package com.shanlihou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import com.shanlihou.adapter.SchoolListAdapter;
import com.shanlihou.schooltao.MainApplication;
import com.shanlihou.schooltao.R;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.*;


/**
 * Created by shanlihou on 2016/6/19.
 */
public class SchoolChoiceActivity extends Activity{
    ListView mSchoolListView;
    SchoolListAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_list);
        mSchoolListView = (ListView)findViewById(R.id.school_list);
        List<Map> list = new ArrayList<>();
        getList(parseJson(text), list, 0);
        mListAdapter = new SchoolListAdapter(this, list);
        mSchoolListView.setAdapter(mListAdapter);
    }
    private Map<String, Object> parseJson(String jsonStr){
        JSONTokener jsonTokener = new JSONTokener(jsonStr);
        JSONObject jsonRet = null;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> province;
        Map<String, Object> city;
        try{
            jsonRet = (JSONObject)jsonTokener.nextValue();
            if (jsonRet.has("schools")){
                JSONArray jsonSchools = jsonRet.getJSONArray("schools");
                int len = jsonSchools.length();
                for(int i = 0; i < len; i ++){
                    JSONObject school = (JSONObject)jsonSchools.get(i);
                    String provinceStr = school.getString("province");

                    if(map.containsKey(provinceStr)){
                        province = (Map<String, Object>)map.get(provinceStr);
                        String cityStr = school.getString("city");

                        if(province.containsKey(cityStr)){
                            city = (Map<String, Object>)province.get(cityStr);
                            city.put(school.getString("school_name"), null);
                        }else{
                            city = new HashMap<>();
                            city.put(school.getString("school_name"), null);
                            province.put(cityStr, city);
                        }
                    }else{
                        province = new HashMap<>();
                        city = new HashMap<>();
                        city.put(school.getString("school_name"), null);
                        province.put(school.getString("city"), city);
                        map.put(provinceStr, province);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
    private void getList(Map<String, Object> map, List<Map> list, int deep){
        Set<Map.Entry<String, Object>> set = map.entrySet();
        Iterator<Map.Entry<String, Object>> iter = set.iterator();
        while(iter.hasNext()){
            Map.Entry<String, Object> entry = iter.next();
            Map<String, Object> tmpMap = new HashMap<>();
            list.add(tmpMap);
            tmpMap.put("name", entry.getKey().toString());
            tmpMap.put("deep", deep);
            Log.d("shanlihou", entry.getKey().toString() + ":" + deep);
            if (entry.getValue() != null){
                getList((Map<String, Object>)entry.getValue(), list, deep + 1);
            }
        }
    }
    String text = "{\"schools\":[{\"city\":\"杭州市\",\"province\":\"浙江省\",\"schoolIndex\":1,\"school_name\":\"杭州电子科技大学\"},{\"city\":\"北京\",\"province\":\"北京\",\"schoolIndex\":2,\"school_name\":\"清华大学\"},{\"city\":\"南京\",\"province\":\"江苏\",\"schoolIndex\":3,\"school_name\":\"南京大学\"},{\"city\":\"南京\",\"province\":\"江苏\",\"schoolIndex\":4,\"school_name\":\"南京理工大学\"},{\"city\":\"南京\",\"province\":\"江苏\",\"schoolIndex\":5,\"school_name\":\"南京理工大学（江北学院）\"},{\"city\":\"杭州\",\"province\":\"浙江\",\"schoolIndex\":6,\"school_name\":\"杭州电子科技大学（信息工程学院）\"},{\"city\":\"宁波\",\"province\":\"浙江\",\"schoolIndex\":7,\"school_name\":\"宁波大学\"},{\"city\":\"温州\",\"province\":\"浙江\",\"schoolIndex\":8,\"school_name\":\"温州大学\"},{\"city\":\"上海\",\"province\":\"上海\",\"schoolIndex\":9,\"school_name\":\"上海交通大学\"},{\"city\":\"剑桥\",\"province\":\"mit\",\"schoolIndex\":10,\"school_name\":\"麻省理工大学\"},{\"city\":\"广州\",\"province\":\"广东\",\"schoolIndex\":11,\"school_name\":\"中山大学\"},{\"city\":\"北京\",\"province\":\"北京\",\"schoolIndex\":12,\"school_name\":\"北京大学\"},{\"city\":\"淮安\",\"province\":\"江苏\",\"schoolIndex\":13,\"school_name\":\"淮安师范学院\"}]}";
}
