package com.shopmall.dry.shopmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.activity.WareListActivity;
import com.shopmall.dry.shopmall.adapter.HomeCatgoryAdapter;
import com.shopmall.dry.shopmall.bean.Banner;
import com.shopmall.dry.shopmall.bean.Campaign;
import com.shopmall.dry.shopmall.bean.HomeCampaign;
import com.shopmall.dry.shopmall.http.BaseCallback;
import com.shopmall.dry.shopmall.http.OkHttpHelper;
import com.shopmall.dry.shopmall.http.SpotsCallBack;
import com.shopmall.dry.shopmall.widget.Contants;
import com.shopmall.dry.shopmall.widget.DividerItemDecortion;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
//import android.app.Fragment;

/**
 * Created by DrY on 2017/6/20.
 */

public class HomeFragment extends BaseFragment{

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    private HomeCatgoryAdapter mAdatper;
    private static  final  String TAG="HomeFragment";
    private List<Banner> mBanner;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void init() {
        requestImages();
        initRecyclerView();
    }

    private void requestImages(){

        String url ="http://112.124.22.238:8081/course_api/banner/query?type=1";

        httpHelper.get(url, new SpotsCallBack<List<Banner>>(getActivity()) {
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private void initRecyclerView() {

        httpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {

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
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    private  void initData(List<HomeCampaign> homeCampaigns){

        //初始化
        mAdatper = new HomeCatgoryAdapter(homeCampaigns,getActivity());

        mAdatper.setOnCampaignClickListener(new HomeCatgoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {

                //Toast.makeText(getContext(),"title="+campaign.getTitle(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID,campaign.getId());
                Log.d(TAG, "onClick() returned: " + campaign.getId());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdatper);
        mRecyclerView.addItemDecoration(new DividerItemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    private void initSlider() {

        if(mBanner != null){

            for(Banner banner : mBanner){
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }
        }

        //设置指示器的类型及位置
        //mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //设置动画
        //mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        //mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        //mSliderLayout.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //设置图片切换时间
        mSliderLayout.setDuration(3000);
    }

    //当界面销毁时停止slider
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSliderLayout.stopAutoCycle();
    }

}
