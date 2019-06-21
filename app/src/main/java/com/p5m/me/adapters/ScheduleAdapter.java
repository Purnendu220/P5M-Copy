package com.p5m.me.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.fragment.ClassMiniViewList;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private String[] titleTabs;

    public ScheduleAdapter(FragmentManager fm, String[] titleTabs) {
        super(fm);
        this.titleTabs = titleTabs;
        fragments = new ArrayList<>(titleTabs.length);

        for (int index = 0; index < titleTabs.length; index++) {
            fragments.add(null);
        }
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public Fragment getItem(int position) {

        int shownInScreen = AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING;

        if (position == AppConstants.Tab.TAB_MY_SCHEDULE_UPCOMING) {
            shownInScreen = AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING;
        } else if (position == AppConstants.Tab.TAB_MY_SCHEDULE_WISH_LIST) {
            shownInScreen = AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST;
        }

        Fragment tabFragment = ClassMiniViewList.createFragment(position, shownInScreen);

        fragments.set(position, tabFragment);
        return tabFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleTabs[position];
    }

    @Override
    public int getCount() {
        return titleTabs.length;
    }
}