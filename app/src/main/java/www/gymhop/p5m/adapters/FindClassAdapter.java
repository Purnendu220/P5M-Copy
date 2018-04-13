package www.gymhop.p5m.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.fragment.ClassList;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class FindClassAdapter extends FragmentStatePagerAdapter {

    private int totalTabs;

    private List<Fragment> fragments;
    private List<GregorianCalendar> calendarList;

    public FindClassAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;

        fragments = new ArrayList<>(totalTabs);

        for (int index = 0; index < totalTabs; index++) {
            fragments.add(null);
        }
    }

    public List<GregorianCalendar> getCalendarList() {
        return calendarList;
    }

    public void setCalendarList(List<GregorianCalendar> calendarList) {
        this.calendarList = calendarList;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    @Override
    public Fragment getItem(int position) {

        GregorianCalendar gregorianCalendar = calendarList.get(position);
        String date = "";
        if (gregorianCalendar != null) {
            date = String.format("%1$tY-%1$tm-%1$td", gregorianCalendar);
        }

        Fragment tabFragment = ClassList.getInstance(date, position, AppConstants.AppNavigation.SHOWN_IN_FIND_CLASS);

        fragments.set(position, tabFragment);
        return tabFragment;
    }


//    @Override
//    public CharSequence getPageTitle(int position) {
//        return "Wed\nMar 17";
//    }
//
//    public int getTotalTabs() {
//        return totalTabs;
//    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}