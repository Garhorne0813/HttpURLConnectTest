package com.garhorne.jsonarraytest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class GoodsAdapter extends ArrayAdapter<Goods> {

    private int resourceId;

    public GoodsAdapter(@NonNull Context context, int resource, @NonNull List<Goods> objects) {
        super(context, resource, objects);
        resourceId = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Goods goods = (Goods) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView)view.findViewById(R.id.name);
            viewHolder.tv_detail = (TextView)view.findViewById(R.id.detail);
            viewHolder.tv_price = (TextView)view.findViewById(R.id.price);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tv_name.setText(goods.getGoodName());
        viewHolder.tv_detail.setText(goods.getDetail());
        viewHolder.tv_price.setText(String.valueOf(goods.getGoodPrice()));

        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_price;
        TextView tv_detail;
    }
}
