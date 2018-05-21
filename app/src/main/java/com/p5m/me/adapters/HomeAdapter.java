package com.p5m.me.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.fragment.FindClass;
import com.p5m.me.view.fragment.MyProfile;
import com.p5m.me.view.fragment.MySchedule;
import com.p5m.me.view.fragment.Trainers;

public class HomeAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private int tabsCount;

    public HomeAdapter(FragmentManager fm, int tabsCount) {
        super(fm);
        this.tabsCount = tabsCount;
        fragments = new ArrayList<>(tabsCount);

        for (int index = 0; index < tabsCount; index++) {
            fragments.add(null);
        }
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;

        if (position == AppConstants.Tab.TAB_FIND_CLASS) {
            frag = new FindClass();
        } else if (position == AppConstants.Tab.TAB_TRAINER) {
            frag = new Trainers();
        } else if (position == AppConstants.Tab.TAB_SCHEDULE) {
            frag = new MySchedule();
        } else if (position == AppConstants.Tab.TAB_MY_PROFILE) {
            frag = new MyProfile();
        }

        fragments.set(position, frag);
        return frag;
    }

    @Override
    public int getCount() {
        return tabsCount;
    }
}