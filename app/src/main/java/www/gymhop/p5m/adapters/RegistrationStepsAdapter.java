package www.gymhop.p5m.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.fragment.RegistrationSteps;

public class RegistrationStepsAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    private int tabsCount;

    public RegistrationStepsAdapter(FragmentManager fm, int tabsCount) {
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

        if (position == 0) {
            frag = RegistrationSteps.createFragment(AppConstants.FragmentPosition.REGISTRATION_STEP_NAME);
        } else if (position == 1) {
            frag = RegistrationSteps.createFragment(AppConstants.FragmentPosition.REGISTRATION_STEP_EMAIL);
        } else if (position == 2) {
            frag = RegistrationSteps.createFragment(AppConstants.FragmentPosition.REGISTRATION_STEP_PASSWORD);
        } else if (position == 3) {
            frag = RegistrationSteps.createFragment(AppConstants.FragmentPosition.REGISTRATION_STEP_GENDER);
        }

        fragments.set(position, frag);
        return frag;
    }

    @Override
    public int getCount() {
        return tabsCount;
    }
}