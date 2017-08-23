package com.shopmall.dry.shopmall.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.bean.Wares;
import com.shopmall.dry.shopmall.widget.CartProvider;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by DrY on 2017/7/24.
 */

public class HotAdapter extends SimpleAdapter<Wares> {

    CartProvider provider;

    public HotAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);
        provider = new CartProvider(context);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥"+wares.getPrice());
        viewHolder.getButton(R.id.bt_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.put(wares);
                //ToastUtils.show(context,"已添加到购物车");
                Log.d(TAG, "onClick() returned: " + "已添加到购物车");
            }
        });
    }

    /*public ShoppingCart convertData(Wares item){
        ShoppingCart cart = new ShoppingCart();
        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());
        return cart;
    }*/

    public void resetLayout(int layoutId){
        this.mLayoutResId = layoutId;
        notifyItemRangeChanged(0, getDatas().size());
    }
}
