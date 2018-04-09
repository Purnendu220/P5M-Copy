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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.FindClassAdapter;
import www.gymhop.p5m.utils.DateUtils;
import www.gymhop.p5m.view.activity.Main.FilterActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class FindClass extends BaseFragment implements View.OnClickListener {

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

    private List<GregorianCalendar> calendarList;

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

        calendarList = new ArrayList<>(TOTAL_DATE_TABS);
        generateTabs();

        setToolBar();

        findClassAdapter = new FindClassAdapter(((BaseActivity) activity).getSupportFragmentManager(), TOTAL_DATE_TABS);
        viewPager.setAdapter(findClassAdapter);
        findClassAdapter.setCalendarList(calendarList);
        viewPager.setOffscreenPageLimit(0);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(context, R.color.date_tabs));
        tabLayout.setupWithViewPager(viewPager);

    }

    private void generateTabs() {
        calendarList.clear();
        for (int index = 0; index < TOTAL_DATE_TABS; index++) {

            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.set(GregorianCalendar.DATE, gregorianCalendar.get(GregorianCalendar.DATE) + index);

            calendarList.add(gregorianCalendar);
        }

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                for (int index = 0; index < calendarList.size(); index++) {

                    final GregorianCalendar gregorianCalendar = calendarList.get(index);

                    String monthName = DateUtils.getMonthName(gregorianCalendar.get(GregorianCalendar.MONTH));
                    String weekdayName = DateUtils.getWeekDaysName(gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK));
                    int day = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);

                    tabLayout.getTabAt(index).setCustomView(R.layout.view_date_tab);
                    View customView = tabLayout.getTabAt(index).getCustomView();

                    TextView info = customView.findViewById(R.id.textViewInfo);
                    TextView title = customView.findViewById(R.id.textViewTitle);

                    info.setText(monthName + " " + day);

                    if (index == 0) {
                        title.setText(getString(R.string.today));
                    } else {
                        title.setText(weekdayName);
                    }
                }
            }
        });
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
}
