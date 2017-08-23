package com.shopmall.dry.shopmall.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.shopmall.dry.shopmall.R;
import com.shopmall.dry.shopmall.bean.Tab;
import com.shopmall.dry.shopmall.fragment.CartFragment;
import com.shopmall.dry.shopmall.fragment.HomeFragment;
import com.shopmall.dry.shopmall.fragment.HotFragment;
import com.shopmall.dry.shopmall.fragment.MineFragment;
import com.shopmall.dry.shopmall.fragment.SortFragment;
import com.shopmall.dry.shopmall.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.realtabcontent)
    FrameLayout realtabcontent;
    @BindView(android.R.id.tabhost)
    FragmentTabHost tabhost;
    private LayoutInflater inflater;
    private List<Tab> mTab = new ArrayList<>(5);
    //private DrToolBar mToolbar;
    private CartFragment cartFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //initToolbar();
        iniTab();

    }

//    private void initToolbar() {
//        mToolbar = (DrToolBar) findViewById(R.id.toolbar);
//        mToolbar.showSearchView();
//    }

    private void iniTab() {

        Tab tab_home = new Tab(R.string.home,R.drawable.selector_icon_home,HomeFragment.class);
        Tab tab_we = new Tab(R.string.hot,R.drawable.selector_icon_hot,HotFragment.class);
        Tab tab_discover = new Tab(R.string.sort,R.drawable.selector_icon_sort,SortFragment.class);
        Tab tab_cart = new Tab(R.string.cart,R.drawable.selector_icon_cart,CartFragment.class);
        Tab tab_my = new Tab(R.string.people,R.drawable.selector_icon_my,MineFragment.class);

        mTab.add(tab_home);
        mTab.add(tab_we);
        mTab.add(tab_discover);
        mTab.add(tab_cart);
        mTab.add(tab_my);

        inflater = LayoutInflater.from(this);

        tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);

        for (Tab tab : mTab){
            TabHost.TabSpec tabSpec = tabhost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(buildIndicator(tab));
            tabhost.addTab(tabSpec,tab.getFragment(),null);
        }

        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId == getString(R.string.cart)){
                    refData();
                }
            }
        });

        //去tab的分隔线
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }
        tabhost.setCurrentTab(0);
    }

    private void refData(){
        if(cartFragment ==null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                cartFragment.refData();
            }
        } else {
            cartFragment.refData();
        }
    }

    private View buildIndicator(Tab tab){
        View view = inflater.inflate(R.layout.tab_indicator,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon_tab);
        TextView textView = (TextView) view.findViewById(R.id.tv_indicator);
        imageView.setBackgroundResource(tab.getIcon());
        textView.setText(tab.getTitle());
        return view;
    }
}
