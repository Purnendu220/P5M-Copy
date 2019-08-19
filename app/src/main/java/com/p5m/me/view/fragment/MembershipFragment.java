package com.p5m.me.view.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.p5m.me.R;
import com.p5m.me.adapters.ScheduleAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigConst;
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

public class MembershipFragment extends BaseFragment implements ViewPagerFragmentSelection, ViewPager.OnPageChangeListener, View.OnClickListener {

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.tabs)
    public TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;


    private ScheduleAdapter scheduleAdapter;
    private String[] titleTabs;
    private int tabPosition = -1;

    public MembershipFragment() {
    }

    public static Fragment createScheduleFragment(int position) {
        Fragment tabFragment = new MembershipFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabPosition = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT, AppConstants.Tab.TAB_MEMBERSHIP_MULTI_GYM);
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
        final ClassModel classModel = data.classModel;
        if (classModel != null) {
            try {
//                    context.getResources().getString(R.string.invite_friends)
                DialogUtils.showBasicMessage(context, "",
                        getString(R.string.successfully_joined) + " " + classModel.getTitle()
                        , RemoteConfigConst.INVITE_FRIENDS_VALUE, new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                Helper.shareClass(context, classModel.getClassSessionId(), classModel.getTitle());

                            }
                        }, context.getResources().getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }


    private void setNotificationIcon() {
        int count = MyPreferences.initialize(context).getNotificationCount();

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
        titleTabs = context.getResources().getStringArray(R.array.schedule_list);
        scheduleAdapter = new ScheduleAdapter(getChildFragmentManager(), titleTabs);
        viewPager.setAdapter(scheduleAdapter);
        viewPager.setOffscreenPageLimit(1);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(context, R.color.date_tabs));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(this);

        viewPager.setCurrentItem(tabPosition);
//        onTabSelection(tabPosition);
        setNotificationIcon();
    }

    boolean isLoadingFirstTime = true;

    @Override
    public void onTabSelection(final int position) {
        if (isLoadingFirstTime) {
            isLoadingFirstTime = false;

            viewPager.post(new Runnable() {
                @Override
                public void run() {
                    onPageSelected(tabPosition);
                }
            });
        }
    }

    public void selectTab(int position) {
        try {
            viewPager.setCurrentItem(position);
        } catch (Exception e) {
            e.printStackTrace();
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
            LogUtils.debug("VarunSCHEDULE onPageSelected " + position);
            ((ViewPagerFragmentSelection) scheduleAdapter.getFragments().get(position)).onTabSelection(position);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
            LogUtils.debug("VarunSCHEDULE error onPageSelected " + position + e.getMessage());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imageViewSearch:
                SearchActivity.openActivity(context, activity, view, AppConstants.AppNavigation.NAVIGATION_FROM_SCHEDULE);
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
