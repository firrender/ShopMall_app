package com.shopmall.dry.shopmall.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by DrY on 2017/7/24.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //声明一个数组,传入指定view
    private SparseArray<View> views;
    //item点击事件
    protected BaseAdapter.OnItemClickListener listener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener){
        super(itemView);
        this.listener = listener;
        //实例化views
        this.views = new SparseArray<View>();
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(listener != null) {
            listener.Onclick(v,getLayoutPosition());
        }
    }

    protected  <T extends View> T findView(int id){
        View view = views.get(id);
        if(view == null){
            view = itemView.findViewById(id);
            views.put(id,view);
        }
        return (T)view;
    }

    public View getView(int id){
        return findView(id);
    }

    public TextView getTextView(int id){
        return findView(id);
    }

    public Button getButton(int viewId) {
        return findView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return findView(viewId);
    }



}
