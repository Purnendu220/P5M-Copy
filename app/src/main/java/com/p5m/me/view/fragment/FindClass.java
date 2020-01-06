package com.p5m.me.view.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.p5m.me.R;
import com.p5m.me.adapters.FindClassAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.FilterActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.Main.SearchActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.LanguageUtils.numberConverter;

public class FindClass extends BaseFragment implements ViewPagerFragmentSelection, View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.frameMapView)
    public FrameLayout frameMapView;
    @BindView(R.id.tabs)
    public TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    public TextView textViewNotificationMessageCounter;

    private FindClassAdapter findClassAdapter;
    private int TOTAL_DATE_TABS = 45;

    public static int SELECTED_POSITION = 0;

    private int dp;

    private List<String> calendarList;
    private Calendar todayDate;
    private boolean isFindClass=false;
    private TextView textViewTitle;
    private Fragment mapView;
    private TextView textViewMapOrList;
    private int navigatedFrom=0;


    public FindClass() {
    }
    public static FindClass newInstance(int navigationFrom) {
        FindClass fragment = new FindClass();
        Bundle args = new Bundle();
        args.putInt(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshFragment(int navigatedFrom) {
        MixPanel.trackFindClass(navigatedFrom,isFindClass);
    }

    public interface TabClickListener {

        void onTabClick(int position, String selectedDate);

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

    @Override
    public void onResume() {
        super.onResume();

        boolean shouldRefreshPage = false;

        try {
            Calendar currentDate = Calendar.getInstance();

            if (!(todayDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                    todayDate.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                    todayDate.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DATE))) {

                shouldRefreshPage = true;
            }

            if (findClassAdapter.getFragments().get(0) == null) {
                shouldRefreshPage = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        if (shouldRefreshPage) {
            activity.overridePendingTransition(0, 0);
            HomeActivity.open(context);
            activity.overridePendingTransition(0, 0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newFilter(Events.NewFilter newFilter) {
        checkFilterCount();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_class, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        calendarList = new ArrayList<>(TOTAL_DATE_TABS);
        navigatedFrom = getArguments().getInt(AppConstants.DataKey.NAVIGATED_FROM_INT, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS);
        findClassAdapter = new FindClassAdapter(getChildFragmentManager(), TOTAL_DATE_TABS);
        viewPager.setAdapter(findClassAdapter);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(context, R.color.date_tabs));
        tabLayout.setupWithViewPager(viewPager);

        generateTabs();
        tabLayout.setSelectedTabIndicatorHeight(0);
        setToolBar();

        findClassAdapter.setCalendarList(calendarList);
        viewPager.setOffscreenPageLimit(TOTAL_DATE_TABS);

        viewPager.addOnPageChangeListener(this);

        checkFilterCount();
        todayDate = Calendar.getInstance();

//        MixPanel.trackFindClass(navigatedFrom,isFindClass);
    }

    private void checkFilterCount() {
        if (TempStorage.getFilters().isEmpty()) {
            textViewNotificationMessageCounter.setVisibility(View.INVISIBLE);
        } else {
            textViewNotificationMessageCounter.setVisibility(View.VISIBLE);
            textViewNotificationMessageCounter.setText(LanguageUtils.numberConverter(TempStorage.getFilters().size()));
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

    private void generateTabs() {
        calendarList.clear();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        for (int index = 0; index < TOTAL_DATE_TABS; index++) {

            if (index != 0) {
                gregorianCalendar.set(GregorianCalendar.DATE, gregorianCalendar.get(GregorianCalendar.DATE) + 1);
            }

            calendarList.add(String.format("%1$tY-%1$tm-%1$td", gregorianCalendar));

            String monthName = DateUtils.getMonthName(gregorianCalendar.get(GregorianCalendar.MONTH));
            String weekdayName = DateUtils.getWeekDaysName(gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK));
            int day = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);
            View tabView = LayoutInflater.from(context).inflate(R.layout.item_tabs, null);
            TextView textViewTitle = tabView.findViewById(R.id.textViewTitle);
            TextView textViewSubtitle = tabView.findViewById(R.id.textViewSubtitle);
            ImageView selectedTabImage = tabView.findViewById(R.id.selectedTabImage);


            tabLayout.getTabAt(index).setCustomView(tabView);

            textViewSubtitle.setText(monthName + " " + numberConverter(day));

            if (index == 0) {
                textViewTitle.setText(getString(R.string.today));
            } else {
                textViewTitle.setText(weekdayName);
            }
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

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_find_class, null);

        v.findViewById(R.id.imageViewFilterer).setOnClickListener(this);
        v.findViewById(R.id.imageViewSearch).setOnClickListener(this);
        textViewMapOrList = v.findViewById(R.id.textViewMapOrList);
        textViewMapOrList.setOnClickListener(this);
        textViewTitle = v.findViewById(R.id.textViewTitle);

        textViewNotificationMessageCounter = v.findViewById(R.id.textViewNotificationMessageCounter);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewSearch:
                SearchActivity.openActivity(context, activity, view, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS);
                break;
            case R.id.textViewMapOrList:
                if(!isFindClass) {
                    textViewTitle.setVisibility(View.VISIBLE);
                    textViewTitle.setText(getString(R.string.map));
                    textViewMapOrList.setText(R.string.list);
                    viewPager.setVisibility(View.GONE);
                    frameMapView.setVisibility(View.VISIBLE);

                    mapView =  MapViewFragment.createFragment(calendarList.get(SELECTED_POSITION), SELECTED_POSITION,
                            navigatedFrom);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.add(R.id.frameMapView, mapView).commit();
                    isFindClass=true;
                }
                else{
                    textViewTitle.setVisibility(View.VISIBLE);
                    textViewTitle.setText(getString(R.string.classes));
                    textViewMapOrList.setText(R.string.map);
                    frameMapView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    isFindClass = false;
                }
                break;

            case R.id.imageViewFilterer:
                FilterActivity.openActivity(context, activity, view);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        markSelectedTab(position);
        SELECTED_POSITION = position;
        if(mapView!=null)
            ((MapViewFragment)mapView).onTabClick(position,calendarList.get(SELECTED_POSITION));
        try {
            ((ViewPagerFragmentSelection) findClassAdapter.getFragments().get(position)).onTabSelection(position);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void markSelectedTab(int position) {
        for (int index = 0; index < TOTAL_DATE_TABS; index++) {
            View customView = tabLayout.getTabAt(index).getCustomView();
            ImageView selectedTabImage = customView.findViewById(R.id.selectedTabImage);
            if (position == index) {
                selectedTabImage.setVisibility(View.VISIBLE);

            } else {
                selectedTabImage.setVisibility(View.INVISIBLE);
            }
            tabLayout.getTabAt(index).setCustomView(customView);
        }
    }



}



