package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.p5m.me.R;
import com.p5m.me.adapters.HomeAdapter;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.activity.custom.BottomTapLayout;
import com.p5m.me.view.fragment.ViewPagerFragmentSelection;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements BottomTapLayout.TabListener, ViewPager.OnPageChangeListener {

    public static void open(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void show(Context context, int tabPosition) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        context.startActivity(intent);
    }

    public static Intent createIntent(Context context, int tabPosition, int innerTabPosition) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        intent.putExtra(AppConstants.DataKey.HOME_TABS_INNER_TAB_POSITION, innerTabPosition);
        return intent;
    }

    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    @BindView(R.id.layoutBottomTabs)
    public LinearLayout layoutBottomTabs;

    @BindView(R.id.buyClasses)
    public Button buyClasses;

    private BottomTapLayout bottomTapLayout;
    private HomeAdapter homeAdapter;

    private final int TOTAL_TABS = 4;
    private int INITIAL_POSITION = AppConstants.Tab.TAB_FIND_CLASS;
    private int currentTab =INITIAL_POSITION;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // If coming from a shared link, then open that link after login..
        if (DeepLinkActivity.url != null) {
            overridePendingTransition(0 ,0 );
            DeepLinkActivity.open(context, DeepLinkActivity.url);
            finish();
            return;
        }

        ButterKnife.bind(activity);
        GlobalBus.getBus().register(this);

        handler = new Handler(Looper.getMainLooper());
        setupBottomTabs();

        homeAdapter = new HomeAdapter(((BaseActivity) activity).getSupportFragmentManager(), TOTAL_TABS);
        viewPager.setAdapter(homeAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(TOTAL_TABS);

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(INITIAL_POSITION);
            }
        });

        setNotificationIcon();

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

    private void setNotificationIcon() {
        bottomTapLayout.getTabViewNotification(AppConstants.Tab.TAB_SCHEDULE, MyPreferences.initialize(context).getNotificationCount());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        INITIAL_POSITION = intent.getIntExtra(AppConstants.DataKey.HOME_TAB_POSITION,
                AppConstants.Tab.TAB_FIND_CLASS);

        LogUtils.debug("Home screen onNewIntent " + INITIAL_POSITION);

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(INITIAL_POSITION);
            }
        });
    }

    private void setupBottomTabs() {
        List<BottomTapLayout.Tab> tabList = new ArrayList<>();
        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_FIND_CLASS, R.drawable.find_a_class, R.drawable.find_a_class,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.find_class), context.getString(R.string.find_class)));
        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_TRAINER, R.drawable.trainers, R.drawable.trainers,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.trainer), context.getString(R.string.trainers)));
        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_SCHEDULE, R.drawable.schedule, R.drawable.schedule,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.schedule), context.getString(R.string.schedule)));
        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_MY_PROFILE, R.drawable.profile, R.drawable.profile,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.profile), context.getString(R.string.profile)));

        bottomTapLayout = new BottomTapLayout();
        bottomTapLayout.setup(context, layoutBottomTabs, tabList, this);

        bottomTapLayout.setTab(AppConstants.Tab.TAB_FIND_CLASS);
    }

    @Override
    public void onPositionChange(int currentPosition, BottomTapLayout.Tab tab, List<BottomTapLayout.Tab> tabList) {
        viewPager.setCurrentItem(tab.id);
    }

    @Override
    public void onReselection(int currentPosition, BottomTapLayout.Tab tab, List<BottomTapLayout.Tab> tabList) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentTab=position;
        try {
            ((ViewPagerFragmentSelection) homeAdapter.getFragments().get(position)).onTabSelection(position);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        bottomTapLayout.setTab(position);
        if(position == AppConstants.Tab.TAB_FIND_CLASS){
            handleBuyClassesButton();
        }
        else{
            buyClasses.setVisibility(View.GONE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private boolean isBackRequested;

    @Override
    public void onBackPressed() {
        if (!isBackRequested) {
            isBackRequested = true;
            ToastUtils.show(context, getString(R.string.press_again_to_exit));
        } else {
            super.onBackPressed();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackRequested = false;
            }
        }, 2000);
    }
    
    private void handleBuyClassesButton(){
        try{
            List<UserPackage> list= TempStorage.getUser().getUserPackageDetailDtoList();
            if(list!=null){
                boolean showBuyClasses=false;
                for (UserPackage packageItem : list) {
                    if(packageItem.getPackageType()!=null && packageItem.getPackageType().equalsIgnoreCase("GENERAL") && packageItem.getBalanceClass()==0){
                        showBuyClasses=true;
                    }

                }
                if(showBuyClasses){
                    buyClasses.setVisibility(View.VISIBLE);
                }else{
                    buyClasses.setVisibility(View.GONE);
                }
            }else{
                buyClasses.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(currentTab == AppConstants.Tab.TAB_FIND_CLASS){
            handleBuyClassesButton();
        }
        else{
            buyClasses.setVisibility(View.GONE);

        }
    }
}
