package www.gymhop.p5m.view.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.TrainersAdapter;
import www.gymhop.p5m.data.main.ClassActivity;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.Main.SearchActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class Trainers extends BaseFragment implements ViewPagerFragmentSelection, ViewPager.OnPageChangeListener, View.OnClickListener {

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

    public Trainers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainers, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

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
        }

        activities.add(0, new ClassActivity("ALL", 0));

        trainersAdapter = new TrainersAdapter(getChildFragmentManager(), activities);
        viewPager.setAdapter(trainersAdapter);
        viewPager.setOffscreenPageLimit(activities.size());

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(context, R.color.date_tabs));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewSearch:
                SearchActivity.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_TRAINERS);
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

}
