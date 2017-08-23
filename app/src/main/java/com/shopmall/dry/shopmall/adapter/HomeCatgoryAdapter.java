package com.shopmall.dry.shopmall.adapter;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.bean.Campaign;
import com.shopmall.dry.shopmall.bean.HomeCampaign;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *
 * Created by DrY on 2017/7/20.
 */

public class HomeCatgoryAdapter extends RecyclerView.Adapter<HomeCatgoryAdapter.ViewHolder> {

    private  static int VIEW_TYPE_L=0;
    private  static int VIEW_TYPE_R=1;
    private LayoutInflater mInflater;
    private  List<HomeCampaign> mDatas;
    private Context mContext;
    private  OnCampaignClickListener mListener;


    public HomeCatgoryAdapter( List<HomeCampaign> datas,Context context){
        mDatas = datas;
        this.mContext = context;
    }

    public void setOnCampaignClickListener(OnCampaignClickListener listener){

        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        if(viewType == VIEW_TYPE_R){

            return  new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2,parent,false));
        }

        return  new ViewHolder(mInflater.inflate(R.layout.template_home_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeCampaign homeCampaign = mDatas.get(position);
        holder.textTitle.setText(homeCampaign.getTitle());
        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(position % 2==0){
            return  VIEW_TYPE_R;
        }
        else return VIEW_TYPE_L;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*HomeCampaign homeCampaign = mDatas.get(getLayoutPosition());
            if(mListener !=null){

                switch (v.getId()) {

                    case R.id.imgview_big:
                        mListener.onClick(v, homeCampaign.getCpOne());
                        break;

                    case R.id.imgview_small_top:
                        mListener.onClick(v, homeCampaign.getCpTwo());
                        break;

                    case R.id.imgview_small_bottom:
                        mListener.onClick(v, homeCampaign.getCpThree());
                        break;

                }
            }*/
            if(mListener !=null){

                anim(v);

            }
        }

        private  void anim(final View v){

            ObjectAnimator animator =  ObjectAnimator.ofFloat(v, "rotationX", 0.0F, 360.0F)
                    .setDuration(200);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    HomeCampaign campaign = mDatas.get(getLayoutPosition());

                    switch (v.getId()){

                        case  R.id.imgview_big:
                            mListener.onClick(v, campaign.getCpOne());
                            break;

                        case  R.id.imgview_small_top:
                            mListener.onClick(v, campaign.getCpTwo());
                            break;

                        case R.id.imgview_small_bottom:
                            mListener.onClick(v,campaign.getCpThree());
                            break;

                    }

                }
            });
            animator.start();
        }
    }

    public  interface OnCampaignClickListener{


        void onClick(View view,Campaign campaign);



    }



}