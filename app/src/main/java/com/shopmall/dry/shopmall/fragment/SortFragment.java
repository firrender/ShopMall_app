package com.shopmall.dry.shopmall.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.adapter.BaseAdapter;
import com.shopmall.dry.shopmall.adapter.SortAdapter;
import com.shopmall.dry.shopmall.adapter.WaresAdapter;
import com.shopmall.dry.shopmall.bean.Banner;
import com.shopmall.dry.shopmall.bean.Category;
import com.shopmall.dry.shopmall.bean.Page;
import com.shopmall.dry.shopmall.bean.Wares;
import com.shopmall.dry.shopmall.http.BaseCallback;
import com.shopmall.dry.shopmall.http.OkHttpHelper;
import com.shopmall.dry.shopmall.http.SpotsCallBack;
import com.shopmall.dry.shopmall.widget.Contants;
import com.shopmall.dry.shopmall.widget.DividerGridItemDecoration;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;
//import android.app.Fragment;

/**
 *
 * Created by DrY on 2017/6/20.
 */

public class SortFragment extends Fragment{

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerviewWares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLaout;
    @ViewInject(R.id.sort_slider)
    private  SliderLayout mSliderLayout;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();
    private SortAdapter mSortAdapter;
    private List<Banner> mBanner;
    private WaresAdapter mAdapter;


    private int currPage=1;
    private int totalPage=1;
    private int pageSize=10;
    private long category_id=0;
    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;
    private int state=STATE_NORMAL;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort,container,false);
        ViewUtils.inject(this,view);

        requestSortData();
        requestBannerData();
        initRefreshLayout();
        return view;
    }
    private  void initRefreshLayout(){
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(currPage <=totalPage)
                    loadMoreData();
                else{
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }

    private  void refreshData(){
        currPage =1;
        state=STATE_REFREH;
        requestWares(category_id);
    }

    private void loadMoreData(){
        currPage = ++currPage;
        state = STATE_MORE;
        requestWares(category_id);
    }

    private void requestSortData() {
        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showSortData(categories);
                if(categories !=null && categories.size()>0)
                    requestWares(categories.get(0).getId());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private  void showSortData(List<Category> categories){

        mSortAdapter = new SortAdapter(getContext(),categories);
        mSortAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void Onclick(View view, int position) {
                Category category = mSortAdapter.getItem(position);
                category_id = category.getId();
                currPage=1;
                state=STATE_NORMAL;
                Log.d(TAG, "Onclick() returned: " + category_id);
                requestWares(category_id);
            }
        });
        mRecyclerView.setAdapter(mSortAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    private void requestBannerData() {
        String url = Contants.API.BANNER+"?type=1";
        mHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                showSliderViews(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showSliderViews(List<Banner> banners){
        if(mBanner != null){
            for(Banner mBanner : banners){
                DefaultSliderView sliderView = new DefaultSliderView(this.getActivity());
                sliderView.image(mBanner.getImgUrl());
                sliderView.description(mBanner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(sliderView);
            }
        }
        mSliderLayout.setDuration(3500);
    }

    private void requestWares(long categoryId){
        String url = Contants.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+currPage+"&pageSize="+pageSize;
        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
            @Override
            public void onRequestBefore(Request request) {
            }

            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(Response request) {
            }

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                Log.d(TAG, "onSuccess() returned: " + "jinqyle ");
                currPage = waresPage.getCurrentPage();
                totalPage =waresPage.getTotalPage();
                showWaresData(waresPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    private  void showWaresData(List<Wares> datas){
        switch (state){
            case  STATE_NORMAL:
                if(mAdapter ==null) {
                    mAdapter = new WaresAdapter(getContext(),datas);
                    mRecyclerviewWares.setAdapter(mAdapter);
                    mRecyclerviewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerviewWares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerviewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));

                } else{
                    mAdapter.clearData();
                    mAdapter.addData(datas);
                }
                break;

            case STATE_REFREH:
                mAdapter.clearData();
                mAdapter.addData(datas);
                mRecyclerviewWares.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;

            case STATE_MORE:
                mAdapter.addData(mAdapter.getDatas().size(),datas);
                mRecyclerviewWares.scrollToPosition(mAdapter.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }
    }

}
