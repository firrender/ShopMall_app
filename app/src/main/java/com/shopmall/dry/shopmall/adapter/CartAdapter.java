package com.shopmall.dry.shopmall.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.bean.ShoppingCart;
import com.shopmall.dry.shopmall.widget.CartProvider;
import com.shopmall.dry.shopmall.widget.NumberAddSubView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by DrY on 2017/7/25.
 */

public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {

    private CheckBox checkBox;
    private TextView textView;
    private CartProvider cartProvider;


    public CartAdapter(Context context, List<ShoppingCart> datas,final CheckBox checkBox,TextView textView) {
        super(context, R.layout.template_cart, datas);
        setCheckBox(checkBox);
        setTextView(textView);
        cartProvider = new CartProvider(context);
        setOnItemClickListener(this);
        showTotalPrice();

    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final ShoppingCart item) {
        viewHolder.getTextView(R.id.text_title).setText(item.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
        CheckBox checkBox = (CheckBox) viewHolder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());

        NumberAddSubView numberAddSubView = (NumberAddSubView) viewHolder.getView(R.id.num_control);
        numberAddSubView.setValue(item.getCount());
        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }
        });
    }

    public void showTotalPrice(){
        float total = getTotalPrice();
        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    private  float getTotalPrice(){
        float sum=0;
        if(!isNull())
            return sum;
        for (ShoppingCart cart: mDatas) {
            if(cart.isChecked())
                sum += cart.getCount()*cart.getPrice();
        }
        return sum;
    }

    @Override
    public void Onclick(View view, int position) {
        ShoppingCart cart =  getItem(position);
        cart.setIsChecked(!cart.isChecked());
        notifyItemChanged(position);
        checkListen();
        showTotalPrice();
    }

    private void checkListen() {
        int count = 0;
        int checkNum = 0;
        if (mDatas != null) {
            count = mDatas.size();
            for (ShoppingCart cart : mDatas) {
                if (!cart.isChecked()) {
                    checkBox.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }
            if (count == checkNum) {
                checkBox.setChecked(true);
            }
        }
    }

    public void setTextView(TextView textview){
        this.textView = textview;
    }

    public void setCheckBox(CheckBox ck){
        this.checkBox = ck;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll_None(checkBox.isChecked());
                showTotalPrice();
            }
        });
    }

    private boolean isNull(){
        return (mDatas !=null && mDatas.size()>0);
    }

    public void checkAll_None(boolean isChecked){

        if(!isNull())
            return ;

        int i=0;
        for (ShoppingCart datas :mDatas){
            datas.setIsChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }

    }



    public void delCart(){
        if(!isNull())
            return ;
        //        for (ShoppingCart cart : datas){
        //            if(cart.isChecked()){
        //                int position = datas.indexOf(cart);
        //                cartProvider.delete(cart);
        //                datas.remove(cart);
        //                notifyItemRemoved(position);
        //            }
        //        }
        for(Iterator iterator = mDatas.iterator(); iterator.hasNext();){
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if(cart.isChecked()){
                int position = mDatas.indexOf(cart);
                cartProvider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }

    }

}
