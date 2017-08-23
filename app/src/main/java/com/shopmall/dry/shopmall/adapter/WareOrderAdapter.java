package com.shopmall.dry.shopmall.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.bean.ShoppingCart;

import java.util.List;




/**
 *
 */
public class WareOrderAdapter extends SimpleAdapter<ShoppingCart> {

    public WareOrderAdapter(Context context, List<ShoppingCart> datas) {
        super(context, R.layout.template_order_wares, datas);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, ShoppingCart item) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
    }

    public float getTotalPrice(){

        float sum=0;
        if(!isNull())
            return sum;

        for (ShoppingCart cart:
                mDatas) {
                sum += cart.getCount()*cart.getPrice();
        }
        return sum;
    }

    private boolean isNull(){
        return (mDatas !=null && mDatas.size()>0);
    }

}
