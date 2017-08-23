package com.shopmall.dry.shopmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.activity.WareDetailActivity;
import com.shopmall.dry.shopmall.adapter.BaseAdapter;
import com.shopmall.dry.shopmall.adapter.HotAdapter;
import com.shopmall.dry.shopmall.bean.Page;
import com.shopmall.dry.shopmall.bean.Wares;
import com.shopmall.dry.shopmall.widget.Contants;
import com.shopmall.dry.shopmall.widget.Pager;

import java.util.List;
//import android.app.Fragment;

/**
 *
 * Created by DrY on 2017/6/20.
 */

public class HotFragment extends BaseFragment implements Pager.OnPageListener {

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLayout;
    private HotAdapter mAdatper;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot,container,false);
    }

    @Override
    public void init() {
        Pager pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_HOT)
                .setLoadMore(true)
                .setOnPageListener(this)
                .setPageSize(20)
                .setRefreshLayout(mRefreshLayout)
                .build(getContext(),new TypeToken<Page<Wares>>(){}.getType());
        pager.request();
    }

    @Override
    public void load(List datas, int totalPage, int totalCount) {
        mAdatper = new HotAdapter(getContext(),datas);
        mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {

            @Override
            public void Onclick(View view, int position) {
                Wares wares = mAdatper.getItem(position);

                Intent intent = new Intent(getActivity(), WareDetailActivity.class);

                intent.putExtra(Contants.WARE,wares);
                startActivity(intent);

            }
        });
        mRecyclerView.setAdapter(mAdatper);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
        mAdatper.clearData();
        mAdatper.addData(datas);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        mAdatper.addData(mAdatper.getDatas().size(),datas);
        mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
    }

    /*private OkHttpHelper httpHelper = OkHttpHelper.getInstance();
        private int currPage = 1;
        private int totalPage = 1;
        private int pageSize = 10;
        //private List<Wares> datas;*/

    //private RecyclerView mRecyclerView;

    /*private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;

    private int state=STATE_NORMAL;*/
    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        ViewUtils.inject(this,view);
        //initRefreshLayout();
        //getData();
        Pager pager = Pager.newBuilder().setUrl(Contants.API.WARES_HOT).setLoadMore(true)
                .setOnPageListener(new Pager.OnPageListener() {
                    @Override
                    public void load(List datas, int totalPage, int totalCount) {
                        mAdatper = new HotAdapter(getContext(),datas);
                        mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {

                            @Override
                            public void Onclick(View view, int position) {
                                Wares wares = mAdatper.getItem(position);

                                Intent intent = new Intent(getActivity(), WareDetailActivity.class);

                                intent.putExtra(Contants.WARE,wares);
                                startActivity(intent);

                            }
                        });
                        mRecyclerView.setAdapter(mAdatper);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                    }

                    @Override
                    public void refresh(List datas, int totalPage, int totalCount) {
                        mAdatper.clearData();
                        mAdatper.addData(datas);
                        mRecyclerView.scrollToPosition(0);
                    }

                    @Override
                    public void loadMore(List datas, int totalPage, int totalCount) {
                        mAdatper.addData(mAdatper.getDatas().size(),datas);
                        mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
                    }
                }).setPageSize(20).setRefreshLayout(mRefreshLayout).build(getContext(),new TypeToken<Page<Wares>>(){}.getType());

        pager.request();

        return view;
    }*/

    /*private void getData() {
        String url = Contants.API.WARES_HOT + "?curPage=" + currPage + "&pageSize=" + pageSize;
        httpHelper.get(url, new SpotsCallBack<Page<Wares>>(getContext()) {
            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                currPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();
                showData(waresPage.getList());
            }
            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }*/

    /*private void showData(List<Wares> datas) {
        switch (state){
            case  STATE_NORMAL:
                mAdatper = new HotAdapter(getContext(),datas);
//                mAdatper.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
//                    @Override
//                    public void Onclick(View view, int positon) {
//
//                    }
//                });
                mRecyclerView.setAdapter(mAdatper);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                break;

            case STATE_REFREH:
                mAdatper.clearData();
                mAdatper.addData(datas);
                mRecyclerView.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;

            case STATE_MORE:
                mAdatper.addData(mAdatper.getDatas().size(),datas);
                mRecyclerView.scrollToPosition(mAdatper.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }
    }*/

    /*private  void initRefreshLayout(){
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
                    //                    Toast.makeText()
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }*/

    /*private  void refreshData(){
        currPage =1;
        state=STATE_REFREH;
        getData();
    }

    private void loadMoreData(){
        currPage = ++currPage;
        state = STATE_MORE;
        getData();
    }*/



}
