package com.p5m.me.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.fragment.GymList;
import com.p5m.me.view.fragment.TrainerList;

import java.util.ArrayList;
import java.util.List;

import com.p5m.me.view.fragment.ClassMiniViewList;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class SearchPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private String[] titleTabs;
    private String searchKeywords = "";

    public SearchPagerAdapter(FragmentManager fm, String[] titleTabs) {
        super(fm);
        this.titleTabs = titleTabs;
        fragments = new ArrayList<>(titleTabs.length);

        for (int index = 0; index < titleTabs.length; index++) {
            fragments.add(null);
        }
    }

    public String getSearchKeywords() {
        return searchKeywords;
    }

    public String[] getTitleTabs() {
        return titleTabs;
    }

    public void setTitleTabs(String[] titleTabs) {
        this.titleTabs = titleTabs;
        fragments = new ArrayList<>(titleTabs.length);

        for (int index = 0; index < titleTabs.length; index++) {
            fragments.add(null);
        }
    }

    public void setSearchKeywords(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment tabFragment = null;

        if (position == 0) {
            tabFragment = ClassMiniViewList.createFragment(searchKeywords, position, AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS);
        }
        if (position == 1) {
            tabFragment = TrainerList.createFragment(searchKeywords, position, AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS);
        }
        if (position == 2) {
            tabFragment = GymList.createFragment(searchKeywords, position, AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS);
        }

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