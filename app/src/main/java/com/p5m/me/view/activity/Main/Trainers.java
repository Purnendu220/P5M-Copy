package com.p5m.me.view.activity.Main;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.p5m.me.R;
import com.p5m.me.adapters.TrainersAdapter;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.fragment.ViewPagerFragmentSelection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.AppConstants.Tab.TAB_EXPLORE_PAGE;

public class Trainers extends BaseActivity implements ViewPagerFragmentSelection, ViewPager.OnPageChangeListener, View.OnClickListener, NetworkCommunicator.RequestListener {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, Trainers.class);
        return intent;
    }
    public static void open(Context context) {
        Intent intent = new Intent(context, Trainers.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.tabs)
    public TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private TrainersAdapter trainersAdapter;
    private String[] titleTabs = new String[]{"", ""};

    private List<ClassActivity> activities;


     @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.fragment_trainers);

         onActivityCreated();
     }

    public void onActivityCreated() {

        ButterKnife.bind(activity);

        setToolBar();

        activities = new ArrayList<>();

        try {
            activities.addAll(TempStorage.getActivities());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        if (activities == null) {
            activities = new ArrayList<>();
            networkCommunicator.getActivities(this, true);
        } else {
            setUpPager();
        }
    }

    private void setUpPager() {
        activities.add(0, new ClassActivity(getString(R.string.all), 0));

        Iterator<ClassActivity> carsIterator = activities.iterator();
        while (carsIterator.hasNext()) {
            ClassActivity c = carsIterator.next();
            if (!c.getStatus()) {
                carsIterator.remove();
            }
        }

        trainersAdapter = new TrainersAdapter(getSupportFragmentManager(), activities);
        viewPager.setAdapter(trainersAdapter);

        viewPager.setOffscreenPageLimit(activities.size());

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(context, R.color.date_tabs));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();


        try {
            networkCommunicator.getActivities(this, true);


        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }


    }

    @Override
    public void onBackPressed() {
        this.finish();
        HomeActivity.show(context, TAB_EXPLORE_PAGE, AppConstants.AppNavigation.SHOWN_IN_HOME_TRAINERS);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewSearch:
                SearchActivity.openActivity(context, activity, view, AppConstants.AppNavigation.NAVIGATION_FROM_TRAINERS);
                break;
        }
    }

    boolean isLoadingFirstTime = true;

    @Override
    public void onTabSelection(int position) {
        if (isLoadingFirstTime) {
            isLoadingFirstTime = false;
            viewPager.post(new Runnable() {
                @Override
                public void run() {
                    onPageSelected(0);
                }
            });
        }
    }

    private void setToolBar() {

        BaseActivity activity = (BaseActivity) this.activity;
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_trainers, null);
        v.findViewById(R.id.imageViewSearch).setOnClickListener(this);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        try {
            ((ViewPagerFragmentSelection) trainersAdapter.getFragments().get(position)).onTabSelection(position);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.ALL_CITY: {
                final List<ClassActivity> list = ((ResponseModel<List<ClassActivity>>) response).data;
                if (!list.isEmpty()) {
                    activities.addAll(list);
                    setUpPager();
                }
                break;
            }
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        ToastUtils.showLong(context, errorMessage);
    }
}
