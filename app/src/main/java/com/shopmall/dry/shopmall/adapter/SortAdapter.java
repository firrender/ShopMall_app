package com.shopmall.dry.shopmall.adapter;

import android.content.Context;

import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.bean.Category;

import java.util.List;

/**
 * Created by DrY on 2017/7/24.
 */

public class SortAdapter extends SimpleAdapter<Category> {

    public SortAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Category category) {
        viewHolder.getTextView(R.id.textView).setText(category.getName());
    }
}
