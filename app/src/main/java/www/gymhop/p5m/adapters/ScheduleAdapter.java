package www.gymhop.p5m.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.fragment.ClassMiniViewList;

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

        if (position == 0) {
            shownInScreen = AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING;
        } else if (position == 1) {
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