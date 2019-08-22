package com.p5m.me.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.fragment.ClassMiniViewList;
import com.p5m.me.view.fragment.MembershipList;

import java.util.ArrayList;
import java.util.List;

import static com.p5m.me.utils.AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS;

public class MembershipTabAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private String[] titleTabs;

    public MembershipTabAdapter(FragmentManager fm, String[] titleTabs) {
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

        int shownInScreen = AppConstants.AppNavigation.SHOWN_IN_MEMBERSHIP_MULTI_GYM;

        if (position == AppConstants.Tab.TAB_MEMBERSHIP_SINGLE_GYM) {
            shownInScreen = AppConstants.AppNavigation.SHOWN_IN_MEMBERSHIP_SINGLE_GYM;
        } else if (position == AppConstants.Tab.TAB_MEMBERSHIP_MULTI_GYM) {
            shownInScreen = AppConstants.AppNavigation.SHOWN_IN_MEMBERSHIP_MULTI_GYM;
        }

        Fragment tabFragment = MembershipList.createFragment(position, shownInScreen);

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