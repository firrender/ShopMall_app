package com.shopmall.dry.shopmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by DrY on 2017/7/24.
 */

public abstract class BaseAdapter<T,H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mDatas;
    protected final Context mContext;
    protected LayoutInflater mInflater;
    protected int mLayoutResId;
    protected OnItemClickListener listener;

    public BaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseAdapter(Context context,int layoutResId,List<T> datas){
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutResId = layoutResId;
        //初始化
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T t = getItem(position);
        bindData((H)holder,t);


    }

    public interface OnItemClickListener{
        void Onclick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    //传入布局文件
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mLayoutResId,null,false);
        return new BaseViewHolder(view,listener);
    }

    @Override
    public int getItemCount() {
        if(mDatas==null || mDatas.size()<=0)
            return 0;
        return mDatas.size();
    }

    public T getItem(int position){
        if (position >= mDatas.size()) return null;
        return mDatas.get(position);
    }

    public abstract void bindData(H viewHolder,T t);

    public List<T> getDatas(){
        return  mDatas;
    }

    public void clearData(){
        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }

    public void addData(List<T> datas){
        addData(0,datas);
    }

    public void addData(int position,List<T> datas){
        if(datas !=null && datas.size()>0) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }
    }

    public void refreshData(List<T> list){
        if(list !=null && list.size()>0){
            clearData();
            int size = list.size();
            for (int i=0;i<size;i++){
                mDatas.add(i,list.get(i));
                notifyItemInserted(i);
            }
        }
    }

    public void loadMoreData(List<T> list){
        if(list !=null && list.size()>0){
            int size = list.size();
            int begin = mDatas.size();
            for (int i=0;i<size;i++){
                mDatas.add(list.get(i));
                notifyItemInserted(i+begin);
            }
        }
    }


}
