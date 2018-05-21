package www.gymhop.p5m.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.fragment.ClassMiniViewList;
import www.gymhop.p5m.view.fragment.GymList;
import www.gymhop.p5m.view.fragment.TrainerList;

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