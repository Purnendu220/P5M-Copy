package www.gymhop.p5m.view.fragment;


import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.FindClassAdapter;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.DateUtils;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.Main.FilterActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class FindClass extends BaseFragment implements ViewPagerFragmentSelection, View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.tabs)
    public TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    public TextView textViewNotificationMessageCounter;

    private FindClassAdapter findClassAdapter;
    private int TOTAL_DATE_TABS = 45;
//    private int TOTAL_DATE_TABS = 7;

    private int dp;

    private List<String> calendarList;

    public FindClass() {
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

        findClassAdapter = new FindClassAdapter(getChildFragmentManager(), TOTAL_DATE_TABS);
        viewPager.setAdapter(findClassAdapter);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(context, R.color.date_tabs));
        tabLayout.setupWithViewPager(viewPager);

        generateTabs();
        setToolBar();

        findClassAdapter.setCalendarList(calendarList);
        viewPager.setOffscreenPageLimit(TOTAL_DATE_TABS);

        viewPager.addOnPageChangeListener(this);
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

    @Override
    public void onResume() {
        super.onResume();
        if (TempStorage.getFilters().isEmpty()) {
            textViewNotificationMessageCounter.setVisibility(View.INVISIBLE);
        } else {
            textViewNotificationMessageCounter.setVisibility(View.VISIBLE);
            textViewNotificationMessageCounter.setText(String.valueOf(TempStorage.getFilters().size()));
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

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);

            TextView textViewTitle = new TextView(context);
            textViewTitle.setGravity(Gravity.CENTER);
            textViewTitle.setTypeface(Typeface.SANS_SERIF);
            textViewTitle.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            textViewTitle.setAllCaps(true);
            textViewTitle.setTextColor(ContextCompat.getColorStateList(context, R.color.date_tabs));
            textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            TextView textViewSubtitle = new TextView(context);
            textViewSubtitle.setPadding(dp, dp * 2, dp, dp);
            textViewTitle.setGravity(Gravity.CENTER);
            textViewSubtitle.setTypeface(Typeface.SANS_SERIF);
            textViewSubtitle.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            textViewSubtitle.setTextColor(ContextCompat.getColorStateList(context, R.color.date_tabs));
            textViewSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

            linearLayout.addView(textViewTitle);
            linearLayout.addView(textViewSubtitle);

            tabLayout.getTabAt(index).setCustomView(linearLayout);

            textViewSubtitle.setText(monthName + " " + day);

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
        textViewNotificationMessageCounter = v.findViewById(R.id.textViewNotificationMessageCounter);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewSearch:
                break;

            case R.id.imageViewFilterer:
                FilterActivity.openActivity(context);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
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
}
