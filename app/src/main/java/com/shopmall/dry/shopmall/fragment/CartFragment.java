package com.shopmall.dry.shopmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.activity.OrderActivity;
import com.shopmall.dry.shopmall.adapter.CartAdapter;
import com.shopmall.dry.shopmall.bean.ShoppingCart;
import com.shopmall.dry.shopmall.widget.CartProvider;

import java.util.List;
//import android.app.Fragment;

/**
 *
 * Created by DrY on 2017/6/20.
 */

public class CartFragment extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;
    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;
    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;
    @ViewInject(R.id.btn_del)
    private Button mBtnDel;
    @ViewInject(R.id.toolbar_edit)
    private Button mToolEdit;

    private CartProvider cartProvider;
    private CartAdapter mAdapter;
    public static final int ACTION_EDIT=1;
    public static final int ACTION_CAMPLATE=2;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart,container,false);
    }

    @Override
    public void init() {
        cartProvider = new CartProvider(getActivity());
        mToolEdit.setOnClickListener(this);
        mToolEdit.setTag(ACTION_EDIT);
        mBtnOrder.setOnClickListener(this);
        showData();
    }

    private void showData(){
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter = new CartAdapter(getContext(),carts,mCheckBox,mTextTotal);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    public void refData(){
        mAdapter.clearData();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_edit:
                int action = (int) v.getTag();
                if(ACTION_EDIT == action){
                    showDelControl();
                }
                else if(ACTION_CAMPLATE == action){
                    hideDelControl();
                }
                break;
            case R.id.btn_order:
                Intent intent = new Intent(getActivity(), OrderActivity.class);

                startActivity(intent,false);
                break;
        }

    }

    @OnClick(R.id.btn_del)
    public void delCart(View view){

        mAdapter.delCart();
    }

    private void showDelControl(){
        mToolEdit.setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolEdit.setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);
    }

    private void  hideDelControl(){
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDel.setVisibility(View.GONE);
        mToolEdit.setText("编辑");
        mToolEdit.setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();
        mCheckBox.setChecked(true);
    }



}
