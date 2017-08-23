package com.shopmall.dry.shopmall.widget;

import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.shopmall.dry.shopmall.bean.Page;
import com.shopmall.dry.shopmall.http.OkHttpHelper;
import com.shopmall.dry.shopmall.http.SpotsCallBack;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *      分页工具类
 * Created by DrY on 2017/7/26.
 */

public class Pager {

    private static Builder builder;
    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;
    private int state=STATE_NORMAL;
    private OkHttpHelper httpHelper;

    private Pager(){
        httpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public void request(){
        requestData();
    }

    public void putParam(String key,Object value){
        builder.params.put(key,value);
    }

    public static Builder newBuilder(){
        builder = new Builder();
        return builder;
    }

    //需要变化的参数
    public static class Builder{

        private String url;
        private Type mType;
        private Context mContext;
        private int currPage = 1;
        private int totalPage = 1;
        private int pageSize = 10;
        private boolean canLoadMore;
        private OnPageListener onPageListener;
        private MaterialRefreshLayout mRefreshLayout;
        private HashMap<String,Object> params = new HashMap<>(5);

        public Builder setUrl(String url){
            this.url = url;
            return builder;
        }

        /*public Builder setCurrPage(int currPage){
            this.currPage = currPage;
            return builder;
        }

        public Builder setTotaPage(int totalPage){
            this.totalPage = totalPage;
            return builder;
        }*/

        public Builder setPageSize(int pageSize){
            this.pageSize = pageSize;
            return builder;
        }

        public Builder setLoadMore(boolean loadMore){
            this.canLoadMore = loadMore;
            return builder;
        }

        public Builder setOnPageListener(OnPageListener onPageListener) {
            this.onPageListener = onPageListener;
            return builder;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout){
            this.mRefreshLayout = refreshLayout;
            return builder;
        }

        public Builder putParam(String key,Object value){
            params.put(key,value);
            return builder;
        }

        public Pager build(Context context,Type type){
            this.mContext = context;
            this.mType = type;
            valid();
            return new Pager();
        }

        //判断不能为空
        private void valid(){
            if(this.mContext==null)
                throw  new RuntimeException("content can't be null");
            if(this.url==null || "".equals(this.url))
                throw  new RuntimeException("url can't be  null");
            if(this.mRefreshLayout==null)
                throw  new RuntimeException("MaterialRefreshLayout can't be  null");
        }


    }

    //以下是固定不变的参数
    /**
     * 显示数据
     */
    private <T> void showData(List<T> datas,int totalPage,int totalCount) {
        switch (state){
            case  STATE_NORMAL:
                if(builder.onPageListener != null){
                    builder.onPageListener.load(datas,totalPage,totalCount);
                }
                break;

            case STATE_REFREH:
                if(builder.onPageListener != null){
                    builder.onPageListener.refresh(datas,totalPage,totalCount);
                }
                builder.mRefreshLayout.finishRefresh();
                break;

            case STATE_MORE:
                if(builder.onPageListener != null){
                    builder.onPageListener.loadMore(datas,totalPage,totalCount);
                }
                builder.mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    private void initRefreshLayout(){
        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
                refresh();
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(builder.currPage<builder.totalPage)
                    loadMore();
                else{
                    Toast.makeText(builder.mContext, "无更多数据", Toast.LENGTH_LONG).show();
                    materialRefreshLayout.finishRefreshLoadMore();
                    materialRefreshLayout.setLoadMore(false);
                }
            }
        });
    }
    /**
     * 刷新数据
     */
    private void refresh(){
        state=STATE_REFREH;
        builder.currPage =1;
        requestData();
    }
    /**
     * 加载数据
     */
    private void loadMore(){
        state=STATE_MORE;
        builder.currPage =++builder.currPage;
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        httpHelper.get(buildUrl(), new RequestCallBack(builder.mContext));
    }

    /**
     * 构建URL地址和参数
     */
    private String buildUrl(){
        return builder.url +"?"+buildUrlParams();
    }

    private String buildUrlParams() {
        HashMap<String, Object> map = builder.params;
        map.put("curPage",builder.currPage);
        map.put("pageSize",builder.pageSize);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0,s.length()-1);
        }
        return s;
    }

    public interface  OnPageListener<T>{
        void load(List<T> datas,int totalPage,int totalCount);
        void refresh(List<T> datas,int totalPage,int totalCount);
        void loadMore(List<T> datas,int totalPage,int totalCount);
    }

    class  RequestCallBack<T> extends SpotsCallBack<Page<T>>{

        public RequestCallBack(Context context) {
            super(context);
            super.type = builder.mType;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            dismissDialog();
            Toast.makeText(builder.mContext,"请求出错："+e.getMessage(),Toast.LENGTH_LONG).show();
            if(STATE_REFREH==state)   {
                builder.mRefreshLayout.finishRefresh();
            }
            else  if(STATE_MORE == state){
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }

        @Override
        public void onSuccess(Response response, Page<T> tPage) {
            builder.currPage = tPage.getCurrentPage();
            builder.pageSize = tPage.getPageSize();
            builder.totalPage = tPage.getTotalPage();
            showData(tPage.getList(),tPage.getTotalPage(),tPage.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {
            Toast.makeText(builder.mContext,"加载数据失败",Toast.LENGTH_LONG).show();
            if(STATE_REFREH==state)   {
                builder.mRefreshLayout.finishRefresh();
            } else  if(STATE_MORE == state){
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }
    }

}
