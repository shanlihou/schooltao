package com.shanlihou.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shanlihou.schooltao.R;

import java.util.List;
import java.util.Map;

/**
 * Created by shanlihou on 2016/6/20.
 */
public class SchoolListAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    List<Map> mList;
    ColorStateList mSchoolTextColor;
    public SchoolListAdapter(Context context, List<Map> list){
        mInflater = LayoutInflater.from(context);
        mList = list;
        Resources resource = (Resources) context.getResources();
        mSchoolTextColor = (ColorStateList) resource.getColorStateList(R.color.school_text);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.school_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.shool_item_text);
            holder.linear = (LinearLayout)convertView.findViewById(R.id.school_item_linear);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView.setText(mList.get(position).get("name").toString());
        int deep = ((int)mList.get(position).get("deep"));
        if (deep == 0){
            holder.linear.setBackgroundResource(R.color.province_background);
        }else if(deep == 2){
            holder.textView.setTextColor(mSchoolTextColor);
        }
        return convertView;
    }
    private class ViewHolder{
        LinearLayout linear;
        TextView textView;
    }
}
