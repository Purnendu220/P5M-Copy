package com.p5m.me.view.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.adapters.ScheduleAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.helper.Helper;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.Main.NotificationActivity;
import com.p5m.me.view.activity.Main.SearchActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySchedule extends BaseFragment implements ViewPagerFragmentSelection, ViewPager.OnPageChangeListener, View.OnClickListener {

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.tabs)
    public TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    public ImageView imageViewNotification;
    public TextView textViewNotificationMessageCounter;

    private ScheduleAdapter scheduleAdapter;
    private String[] titleTabs ;
//        private String[] titleTabs = new String[]{"UPCOMING","WISH LIST"};
    private boolean isVisibleToUser=false;

    public MySchedule() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notificationReceived(Events.NotificationCountUpdated notificationCountUpdated) {
        setNotificationIcon();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ClassAutoJoin(Events.ClassAutoJoin data) {
        final ClassModel classModel=data.classModel;
            if(classModel!=null){
                try{
                    DialogUtils.showBasicMessage(context,"",
                            getString(R.string.successfully_joined)+" " + classModel.getTitle()
                            ,context.getResources().getString(R.string.invite_friends), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    Helper.shareClass(context, classModel.getClassSessionId(), classModel.getTitle());

                                }
                            },  context.getResources().getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            });
                }catch (Exception e){
                    e.printStackTrace();
                }


            }


    }
    private void setNotificationIcon() {
        int count = MyPreferences.initialize(context).getNotificationCount();

        textViewNotificationMessageCounter.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        textViewNotificationMessageCounter.setText(String.valueOf(count));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_schedule, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        setToolBar();
        titleTabs= context.getResources().getStringArray(R.array.schedule_list);
        scheduleAdapter = new ScheduleAdapter(getChildFragmentManager(), titleTabs);
        viewPager.setAdapter(scheduleAdapter);
        viewPager.setOffscreenPageLimit(1);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(context, R.color.date_tabs));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(this);

        setNotificationIcon();
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

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_my_schedule, null);
        imageViewNotification = v.findViewById(R.id.imageViewNotification);
        textViewNotificationMessageCounter = v.findViewById(R.id.textViewNotificationMessageCounter);

        v.findViewById(R.id.imageViewSearch).setOnClickListener(this);
        imageViewNotification.setOnClickListener(this);

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
            ((ViewPagerFragmentSelection) scheduleAdapter.getFragments().get(position)).onTabSelection(position);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewNotification:
                NotificationActivity.openActivity(context);
                MixPanel.trackNotificationVisit(AppConstants.Tracker.FROM_MY_SCHEDULE);
                break;
            case R.id.imageViewSearch:
                SearchActivity.openActivity(context, activity, view, AppConstants.AppNavigation.NAVIGATION_FROM_SCHEDULE);
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser=isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }
}
