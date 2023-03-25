package com.example.tongxunlu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    //使用list<Nate>,list会存储数据库中note表所有记录。
    private List<User> list;
    //用于将某个布局转换为view的对象。
    private LayoutInflater layoutInflater;
    //当创建MyAdapter对象的时候，需要list的数据
    public MyAdapter(List<User> list, Context context){
        this.list=list;
        layoutInflater= LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.layout_item,null,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        //将数据库中的内容加载到对应的控件上
        User user =(User) getItem(position);
        viewHolder.tv_showName.setText(user.getName());
        viewHolder.tv_showPhone.setText(user.getPhone());
        return convertView;
    }
    //用于给item的视图加载数据内容。
    class ViewHolder{
        TextView tv_showName,tv_showPhone;
        public ViewHolder(View view){
            tv_showName=view.findViewById(R.id.tv_showName);
            tv_showPhone=view.findViewById(R.id.tv_showPhone);
        }
    }
}


