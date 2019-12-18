package com.p5m.me.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.p5m.me.adapters.viewholder.ProfileHeaderTabViewHolder;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.fragment.FindClass;
import com.p5m.me.view.fragment.FragmentExplore;
import com.p5m.me.view.fragment.MembershipFragment;
import com.p5m.me.view.fragment.MyProfile;
import com.p5m.me.view.fragment.MySchedule;
import com.p5m.me.view.activity.Main.Trainers;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends FragmentStatePagerAdapter {

    private int myScheduleTabPosition = AppConstants.Tab.TAB_MY_SCHEDULE_UPCOMING;
    private List<Fragment> fragments;
    private int tabsCount;
    private int myProfileTabPosition = ProfileHeaderTabViewHolder.TAB_1;
    private int NAVIGATED_FROM_INT = -AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS;
    private int NUMBER_OF_PACKAGES_TO_BUY = -1;
    private BookWithFriendData BOOK_WITH_FRIEND_DATA;
    private ClassModel CLASS_OBJECT;


    public HomeAdapter(FragmentManager fm, int tabsCount, int myProfileTabPosition, int myScheduleTabPosition) {
        super(fm);
        this.tabsCount = tabsCount;
        this.myProfileTabPosition = myProfileTabPosition;
        this.myScheduleTabPosition = myScheduleTabPosition;
        fragments = new ArrayList<>(tabsCount);

        for (int index = 0; index < tabsCount; index++) {
            fragments.add(null);
        }
    }

    public HomeAdapter(FragmentManager fm, int tabsCount, int myProfileTabPosition, int myScheduleTabPosition, int NAVIGATED_FROM_INT, int NUMBER_OF_PACKAGES_TO_BUY, ClassModel CLASS_OBJECT, BookWithFriendData BOOK_WITH_FRIEND_DATA) {
        super(fm);
        this.tabsCount = tabsCount;
        fragments = new ArrayList<>(tabsCount);
        this.myProfileTabPosition = myProfileTabPosition;
        this.myScheduleTabPosition = myScheduleTabPosition;
        this.NAVIGATED_FROM_INT = NAVIGATED_FROM_INT;
        this.NUMBER_OF_PACKAGES_TO_BUY = NUMBER_OF_PACKAGES_TO_BUY;
        this.CLASS_OBJECT = CLASS_OBJECT;
        this.BOOK_WITH_FRIEND_DATA = BOOK_WITH_FRIEND_DATA;

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

        } else if (position == AppConstants.Tab.TAB_EXPLORE_PAGE) {
            frag = FragmentExplore.createExploreFragment();
        } else if (position == AppConstants.Tab.TAB_SCHEDULE) {
            frag = MySchedule.createScheduleFragment(myScheduleTabPosition);
        } else if (position == AppConstants.Tab.TAB_MY_PROFILE) {
            frag = MyProfile.createMyProfileFragment(myProfileTabPosition);
        } else if (position == AppConstants.Tab.TAB_MY_MEMBERSHIP) {
            if (BOOK_WITH_FRIEND_DATA != null && CLASS_OBJECT != null) {
                frag = MembershipFragment.newInstance(NAVIGATED_FROM_INT, CLASS_OBJECT, BOOK_WITH_FRIEND_DATA, NUMBER_OF_PACKAGES_TO_BUY);
            } else if (CLASS_OBJECT != null && BOOK_WITH_FRIEND_DATA == null) {
                frag = MembershipFragment.newInstance(NAVIGATED_FROM_INT, CLASS_OBJECT);
            } else {
                frag = MembershipFragment.newInstance(NAVIGATED_FROM_INT);

            }
        }


        fragments.set(position, frag);
        return frag;
    }

    @Override
    public int getCount() {
        return tabsCount;
    }


}