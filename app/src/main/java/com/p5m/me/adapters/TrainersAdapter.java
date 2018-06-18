package com.p5m.me.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.fragment.TrainerList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TrainersAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private List<ClassActivity> titleTabs;

    public TrainersAdapter(FragmentManager fm, List<ClassActivity> titleTabs) {
        super(fm);
        this.titleTabs = titleTabs;
        fragments = new ArrayList<>(titleTabs.size());

        for (int index = 0; index < titleTabs.size(); index++) {
            fragments.add(null);
        }
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment tabFragment = TrainerList.createFragment(titleTabs.get(position).getId(),
                position, AppConstants.AppNavigation.SHOWN_IN_HOME_TRAINERS);

        fragments.set(position, tabFragment);
        return tabFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleTabs.get(position).getName();
    }

    @Override
    public int getCount() {
        return titleTabs.size();
    }
}