package com.shopmall.dry.shopmall.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by DrY on 2017/7/24.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {

    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }


    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }


}
