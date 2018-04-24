package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.HomeAdapter;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.activity.base.BaseActivity;
import www.gymhop.p5m.view.activity.custom.BottomTapLayout;

public class HomeActivity extends BaseActivity implements BottomTapLayout.TabListener, ViewPager.OnPageChangeListener {


    public static void open(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    @BindView(R.id.layoutBottomTabs)
    public LinearLayout layoutBottomTabs;

    private BottomTapLayout bottomTapLayout;
    private HomeAdapter homeAdapter;

    private static final int TOTAL_TABS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(activity);

        setupBottomTabs();

        homeAdapter = new HomeAdapter(((BaseActivity) activity).getSupportFragmentManager(), TOTAL_TABS);
        viewPager.setAdapter(homeAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(TOTAL_TABS);

        layoutBottomTabs.post(new Runnable() {
            @Override
            public void run() {
                bottomTapLayout.setTab(AppConstants.FragmentPosition.TAB_FIND_CLASS);
            }
        });
    }

    private void setupBottomTabs() {
        List<BottomTapLayout.Tab> tabList = new ArrayList<>();
        tabList.add(new BottomTapLayout.Tab(AppConstants.FragmentPosition.TAB_FIND_CLASS, R.drawable.find_a_class, R.drawable.find_a_class,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.find_class), context.getString(R.string.find_class)));
        tabList.add(new BottomTapLayout.Tab(AppConstants.FragmentPosition.TAB_TRAINER, R.drawable.trainers, R.drawable.trainers,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.trainer), context.getString(R.string.trainer)));
        tabList.add(new BottomTapLayout.Tab(AppConstants.FragmentPosition.TAB_SCHEDULE, R.drawable.schedule, R.drawable.schedule,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.schedule), context.getString(R.string.schedule)));
        tabList.add(new BottomTapLayout.Tab(AppConstants.FragmentPosition.TAB_MY_PROFILE, R.drawable.profile, R.drawable.profile,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.profile), context.getString(R.string.profile)));

        bottomTapLayout = new BottomTapLayout();
        bottomTapLayout.setup(context, layoutBottomTabs, tabList, this);

        bottomTapLayout.setTab(AppConstants.FragmentPosition.TAB_FIND_CLASS);
    }

    @Override
    public void onPositionChange(int currentPosition, BottomTapLayout.Tab tab, List<BottomTapLayout.Tab> tabList) {
        viewPager.setCurrentItem(tab.id);
    }

    @Override
    public void onReselection(int currentPosition, BottomTapLayout.Tab tab, List<BottomTapLayout.Tab> tabList) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        bottomTapLayout.setTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
