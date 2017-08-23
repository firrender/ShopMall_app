package com.shopmall.dry.shopmall.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.adapter.HotAdapter;
import com.shopmall.dry.shopmall.bean.Page;
import com.shopmall.dry.shopmall.bean.Wares;
import com.shopmall.dry.shopmall.widget.Contants;
import com.shopmall.dry.shopmall.widget.Pager;

import java.util.List;

/**
 *
 * Created by DrY on 2017/7/26.
 */

public class WareListActivity extends AppCompatActivity implements Pager.OnPageListener<Wares>, View.OnClickListener, TabLayout.OnTabSelectedListener{
    private static final String TAG = "WareListActivity";

    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;
    @ViewInject(R.id.txt_summary)
    private TextView mTxtSummary;
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview_wares;
    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;
    @ViewInject(R.id.toolbar_edit)
    private ImageButton mButtonEdit;
    @ViewInject(R.id.toolbar_edit1)
    private ImageButton mButtonEdit1;
    @ViewInject(R.id.toolbar_back)
    private ImageButton mButtonBack;

    public static final int TAG_DEFAULT=0;
    public static final int TAG_SALE=1;
    public static final int TAG_PRICE=2;
    private int orderBy = 0;
    private long campaignId = 0;
    private HotAdapter mAdapter;
    private Pager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warelist);
        ViewUtils.inject(this);

        campaignId = getIntent().getLongExtra(Contants.COMPAINGAIN_ID,0);
        Log.d(TAG, "onCreate() returned: " + campaignId);

        initTab();
        getData();
        mButtonEdit.setOnClickListener(this);
        mButtonEdit1.setOnClickListener(this);
        mButtonBack.setOnClickListener(this);

    }


    private void getData() {
        pager = Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId",campaignId)
                .putParam("orderBy",orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this,new TypeToken<Page<Wares>>(){}.getType());

        pager.request();

    }

    private void initTab(){

        TabLayout.Tab tab= mTablayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);

        mTablayout.addTab(tab);
        tab= mTablayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        mTablayout.addTab(tab);
        mTablayout.setOnTabSelectedListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.toolbar_edit:
                mAdapter.resetLayout(R.layout.template_grid_wares);

                mRecyclerview_wares.setLayoutManager(new GridLayoutManager(this,2));
                mRecyclerview_wares.setAdapter(mAdapter);
                mButtonEdit.setVisibility(View.GONE);
                mButtonEdit1.setVisibility(View.VISIBLE);

                break;

            case R.id.toolbar_edit1:
                mAdapter.resetLayout(R.layout.template_hot_wares);

                mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerview_wares.setAdapter(mAdapter);

                mButtonEdit1.setVisibility(View.GONE);
                mButtonEdit.setVisibility(View.VISIBLE);
                break;

            case R.id.toolbar_back:

                WareListActivity.this.finish();
                break;

        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy",orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {

        mTxtSummary.setText("共有"+totalCount+"件商品");

        if (mAdapter == null) {
            mAdapter = new HotAdapter(this, datas);
           /* mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Wares wares = mAdapter.getItem(position);

                    Intent intent = new Intent(WareListActivity.this, WareDetailActivity.class);

                    intent.putExtra(Contants.WARE,wares);
                    startActivity(intent);
                }
            });*/
            mRecyclerview_wares.setAdapter(mAdapter);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview_wares.setItemAnimator(new DefaultItemAnimator());
            mRecyclerview_wares.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        } else {
            mAdapter.refreshData(datas);
        }

    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter.refreshData(datas);
        mRecyclerview_wares.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter.loadMoreData(datas);
    }
}
