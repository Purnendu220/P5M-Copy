package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.p5m.me.R;
import com.p5m.me.data.FaceBookUser;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.activity.custom.MyViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends BaseActivity {


    public static void open(Context context) {
        context.startActivity(new Intent(context, RegistrationActivity.class));
    }

    public static void open(Context context, FaceBookUser faceBookUser) {
        context.startActivity(new Intent(context, RegistrationActivity.class).putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, AppConstants.AppNavigation.NAVIGATION_FROM_FB_LOGIN));
    }

    @BindView(R.id.imageViewBack)
    public ImageView imageViewBack;
    @BindView(R.id.viewPager)
    public MyViewPager viewPager;
    @BindView(R.id.layoutIndicator)
    public LinearLayout layoutIndicator;

    public int navigatedFrom;
    private int TOTAL_STEPS = AppConstants.Tab.COUNT_NORMAL_REGISTRATION;
    private int INITIAL_STEP = 0;

//    private RegistrationStepsAdapter registrationStepsAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        showActionBar = false;
        super.onCreate(bundle);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(activity);

        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);

        if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FB_LOGIN) {
//            TOTAL_STEPS = AppConstants.Tab.COUNT_FB_REGISTRATION;
            String message = "";

            if (!message.isEmpty()) {
                DialogUtils.showBasicMessage(context, context.getString(R.string.app_name), getString(R.string.ok), message);
            }
        } else {
            TOTAL_STEPS = AppConstants.Tab.COUNT_NORMAL_REGISTRATION;
        }

//        setViewPagerAdapter();
    }

    private void setViewPagerAdapter() {
        // Pager Setup..
//        registrationStepsAdapter = new RegistrationStepsAdapter(getSupportFragmentManager(), TOTAL_STEPS);
//        viewPager.setAdapter(registrationStepsAdapter);
//        viewPager.setOffscreenPageLimit(TOTAL_STEPS);


//        // Un-Scrollable
       viewPager.setPagingEnabled(false);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(INITIAL_STEP);
            }
        });
    }

    public void stepsDone() {
    }

    public void next() {
        if (viewPager.getCurrentItem() != AppConstants.Tab.REGISTRATION_STEP_GENDER) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        if (viewPager.getCurrentItem() != AppConstants.Tab.REGISTRATION_STEP_NAME) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != AppConstants.Tab.REGISTRATION_STEP_NAME) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            super.onBackPressed();
        }
    }
}
