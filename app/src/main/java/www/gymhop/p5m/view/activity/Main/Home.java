package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.view.activity.base.BaseActivity;
import www.gymhop.p5m.view.activity.custom.BottomTapLayout;
import www.gymhop.p5m.view.fragment.FindClass;
import www.gymhop.p5m.view.fragment.MyProfile;
import www.gymhop.p5m.view.fragment.MySchedule;
import www.gymhop.p5m.view.fragment.Trainers;

public class Home extends BaseActivity implements BottomTapLayout.TabListener {

    public static void open(Context context) {
        context.startActivity(new Intent(context, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @BindView(R.id.layoutBottomTabs)
    public LinearLayout layoutBottomTabs;
    @BindView(R.id.layoutFragment)
    public LinearLayout layoutFragment;

    private BottomTapLayout bottomTapLayout;

    private static int TAB_FIND_CLASS = 1;
    private static int TAB_TRAINER = 2;
    private static int TAB_SCHEDULE = 3;
    private static int TAB_MY_PROFILE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(activity);

        setupBottomTabs();
    }

    private void setupBottomTabs() {
        List<BottomTapLayout.Tab> tabList = new ArrayList<>();
        tabList.add(new BottomTapLayout.Tab(TAB_FIND_CLASS, R.drawable.find_a_class, R.drawable.find_a_class,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.find_class), context.getString(R.string.find_class)));
        tabList.add(new BottomTapLayout.Tab(TAB_TRAINER, R.drawable.trainers, R.drawable.trainers,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.trainer), context.getString(R.string.trainer)));
        tabList.add(new BottomTapLayout.Tab(TAB_SCHEDULE, R.drawable.schedule, R.drawable.schedule,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.schedule), context.getString(R.string.schedule)));
        tabList.add(new BottomTapLayout.Tab(TAB_MY_PROFILE, R.drawable.profile, R.drawable.profile,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.profile), context.getString(R.string.profile)));

        bottomTapLayout = new BottomTapLayout();
        bottomTapLayout.setup(context, layoutBottomTabs, tabList, this);

//        ViewCompat.setElevation(layoutBottomTabs, getResources().getDimension(R.dimen.bottom_navigation_elevation));

        bottomTapLayout.setTab(TAB_FIND_CLASS);
    }

    @Override
    public void onPositionChange(int currentPosition, BottomTapLayout.Tab tab, List<BottomTapLayout.Tab> tabList) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        if (tab.id == TAB_FIND_CLASS) {
            Fragment frag = getSupportFragmentManager().findFragmentByTag("FindClass");
            if (frag == null) {
                frag = new FindClass();
            }
            transaction.replace(R.id.layoutFragment, frag, "FindClass").commitNow();
        } else if (tab.id == TAB_TRAINER) {
            Fragment frag = getSupportFragmentManager().findFragmentByTag("Trainers");
            if (frag == null) {
                frag = new Trainers();
            }
            transaction.replace(R.id.layoutFragment, frag, "Trainers").commitNow();
        } else if (tab.id == TAB_SCHEDULE) {
            Fragment frag = getSupportFragmentManager().findFragmentByTag("MySchedule");
            if (frag == null) {
                frag = new MySchedule();
            }
            transaction.replace(R.id.layoutFragment, frag, "MySchedule").commitNow();
        } else if (tab.id == TAB_MY_PROFILE) {
            Fragment frag = getSupportFragmentManager().findFragmentByTag("MyProfile");
            if (frag == null) {
                frag = new MyProfile();
            }
            transaction.replace(R.id.layoutFragment, frag, "MyProfile").commitNow();

            PackageLimits.openActivity(context);
        }
    }

    @Override
    public void onReselection(int currentPosition, BottomTapLayout.Tab tab, List<BottomTapLayout.Tab> tabList) {
    }
}
