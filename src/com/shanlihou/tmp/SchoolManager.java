package com.shanlihou.tmp;

import android.util.Log;
import com.shanlihou.schooltao.MainApplication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.*;

/**
 * Created by shanlihou on 2016/9/27.
 */
public class SchoolManager {
    private static Map<String, Object> mMap = null;
    private SchoolManager(){
    }
    private static SchoolManager mInstance = null;

    public static SchoolManager getInstance() {
        if (mInstance == null){
            mInstance = new SchoolManager();
        }
        return mInstance;
    }

    public boolean getMap(){
        HttpContent req = UrlOpener.getInstance().urlOpen(MainApplication.URL_BASE + "/SchoolListServlet", null);
        if (req == null){
            return false;
        }
        return parseJson2(req.getContent());
    }

    private boolean parseJson2(String jsonStr){
        JSONTokener jsonTokener = new JSONTokener(jsonStr);
        JSONObject jsonRet = null;
        mMap = new HashMap<>();
        Map<String, Object> province;
        Map<String, Object> city;
        try{
            jsonRet = (JSONObject)jsonTokener.nextValue();
            if (jsonRet.has("response")){
                JSONObject jsonProvince = (JSONObject)jsonRet.get("response");
                Iterator itProv = jsonProvince.keys();
                while (itProv.hasNext()){
                    String keyProv = (String)itProv.next();
                    JSONObject jsonCity = (JSONObject)jsonProvince.get(keyProv);
                    Iterator itCity = jsonCity.keys();
                    province = new HashMap<>();
                    while (itCity.hasNext()){
                        String keyCity = (String)itCity.next();
                        JSONArray jsonSchools = jsonCity.getJSONArray(keyCity);
                        city = new HashMap<>();
                        int len = jsonSchools.length();
                        for (int i = 0; i < len; i++){
                            JSONObject jsonSchool = (JSONObject)jsonSchools.get(i);
                            city.put(jsonSchool.getString("school_name"), jsonSchool.getInt("schoolIndex"));
                        }
                        city.put("close", 1);
                        province.put(keyCity, city);
                    }
                    province.put("close", 1);
                    mMap.put(keyProv, province);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private void parseJson(String jsonStr){
        JSONTokener jsonTokener = new JSONTokener(jsonStr);
        JSONObject jsonRet = null;
        mMap = new HashMap<>();
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

                    if(mMap.containsKey(provinceStr)){
                        province = (Map<String, Object>)mMap.get(provinceStr);
                        String cityStr = school.getString("city");

                        if(province.containsKey(cityStr)){
                            city = (Map<String, Object>)province.get(cityStr);
                            city.put(school.getString("school_name"), null);
                        }else{
                            city = new HashMap<>();
                            city.put("close", 1);
                            city.put(school.getString("school_name"), null);
                            province.put(cityStr, city);
                        }
                    }else{
                        province = new HashMap<>();
                        city = new HashMap<>();
                        province.put("close", 1);
                        city.put("close", 1);
                        city.put(school.getString("school_name"), null);
                        province.put(school.getString("city"), city);
                        mMap.put(provinceStr, province);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getSchoolName(int id){
        if (mMap == null && (!getMap())){
            return null;
        }
        return getSchoolNameFromMap(id);
    }
    private String getSchoolNameFromMap(int id){
        Set<Map.Entry<String, Object>> set = mMap.entrySet();
        Iterator<Map.Entry<String, Object>> iter = set.iterator();
        while(iter.hasNext()){
            Map.Entry<String, Object> entry = iter.next();
            if (entry.getKey() == "close"){
                continue;
            }
            Set<Map.Entry<String, Integer>> citySet = ((Map<String, Integer>)(entry.getValue())).entrySet();
            Iterator<Map.Entry<String, Integer>> cityIter = citySet.iterator();
            while(cityIter.hasNext()){
                Map.Entry<String, Integer> cityEntry = cityIter.next();
                Log.d("shanlihou", cityEntry.getKey());
                if (cityEntry.getKey() == "close"){
                    continue;
                }else if(cityEntry.getValue() == id){
                    return cityEntry.getKey();
                }
            }
        }
        return null;
    }
    public List<Map> getList(){
        List<Map> retList = new ArrayList<>();
        map2List(mMap, retList, 0);
        return retList;
    }
    private void map2List(Map<String, Object> map, List<Map> list, int deep){
        if (map.get("close") != null){
            return;
        }
        Set<Map.Entry<String, Object>> set = map.entrySet();
        Iterator<Map.Entry<String, Object>> iter = set.iterator();
        while(iter.hasNext()){
            Map.Entry<String, Object> entry = iter.next();
            if (entry.getKey() == "close"){
                continue;
            }

            Map<String, Object> tmpMap = new HashMap<>();
            list.add(tmpMap);
            tmpMap.put("name", entry.getKey().toString());
            tmpMap.put("deep", deep);
            tmpMap.put("map", entry.getValue());
            if (deep == 2){
                continue;
            }
            if (entry.getValue() != null){
                map2List((Map<String, Object>) entry.getValue(), list, deep + 1);
            }
        }
    }
}
