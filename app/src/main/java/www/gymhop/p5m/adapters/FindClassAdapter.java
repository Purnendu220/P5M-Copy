package www.gymhop.p5m.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.fragment.ClassList;

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