package com.shopmall.dry.shopmall.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.bean.Wares;

import java.util.List;

/**
 *
 */

public class WaresAdapter extends SimpleAdapter<Wares>{

    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_grid_wares, datas);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_sort);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        viewHolder.getTextView(R.id.text_title_sort).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price_sort).setText("￥"+wares.getPrice());
    }
}