package com.p5m.me.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.p5m.me.R;
import com.p5m.me.view.fragment.ClassList;

import java.util.ArrayList;
import java.util.List;

import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.fragment.MapViewFragment;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class FindClassAdapter extends FragmentStatePagerAdapter {

    private int totalTabs;

    private List<Fragment> fragments;
    private List<String> calendarList;

    public FindClassAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;

        fragments = new ArrayList<>(totalTabs);

        for (int index = 0; index < totalTabs; index++) {
            fragments.add(null);
        }
    }

    public List<String> getCalendarList() {
        return calendarList;
    }

    public void setCalendarList(List<String> calendarList) {
        this.calendarList = calendarList;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment tabFragment = ClassList.createFragment(calendarList.get(position), position,
                AppConstants.AppNavigation.SHOWN_IN_HOME_FIND_CLASSES);

        fragments.set(position, tabFragment);

        return tabFragment;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}