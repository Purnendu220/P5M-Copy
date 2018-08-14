package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.p5m.me.R;
import com.p5m.me.adapters.RegistrationStepsAdapter;
import com.p5m.me.data.FaceBookUser;
import com.p5m.me.data.request.RegistrationRequest;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.ViewPagerIndicator;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends BaseActivity {

    public static FaceBookUser faceBookUser;

    public static void open(Context context) {
        RegistrationActivity.faceBookUser = null;
        context.startActivity(new Intent(context, RegistrationActivity.class));
    }

    public static void open(Context context, FaceBookUser faceBookUser) {
        RegistrationActivity.faceBookUser = faceBookUser;
        context.startActivity(new Intent(context, RegistrationActivity.class).putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, AppConstants.AppNavigation.NAVIGATION_FROM_FB_LOGIN));
    }

    @BindView(R.id.imageViewBack)
    public ImageView imageViewBack;
    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.layoutIndicator)
    public LinearLayout layoutIndicator;

    public int navigatedFrom;
    private int TOTAL_STEPS = AppConstants.Tab.COUNT_NORMAL_REGISTRATION;
    private int INITIAL_STEP = 0;

    private RegistrationStepsAdapter registrationStepsAdapter;
    private RegistrationRequest registrationRequest = new RegistrationRequest();

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
            registrationRequest = new RegistrationRequest(faceBookUser.getId(), faceBookUser.getName());

            if (faceBookUser.getEmail().isEmpty()) {

                INITIAL_STEP = AppConstants.Tab.REGISTRATION_STEP_EMAIL;

                if (faceBookUser.getGender().isEmpty()) {
                    message = "P5M needs your Email and Gender";
                } else {
                    message = "P5M needs your Email";
                    registrationRequest.setGender(faceBookUser.getGender());
                }

            } else {
                if (faceBookUser.getGender().isEmpty()) {
                    INITIAL_STEP = AppConstants.Tab.REGISTRATION_STEP_GENDER;

                    message = "P5M needs your Gender";
                } else {
                    registrationRequest.setGender(faceBookUser.getGender());
                }

                registrationRequest.setEmail(faceBookUser.getEmail());
            }

            if (!message.isEmpty()) {
                DialogUtils.showBasicMessage(context, context.getString(R.string.app_name), "ok", message);
            }
        } else {
//            TOTAL_STEPS = AppConstants.Tab.COUNT_NORMAL_REGISTRATION;
        }

        setViewPagerAdapter();
    }

    private void setViewPagerAdapter() {
        // Pager Setup..
        registrationStepsAdapter = new RegistrationStepsAdapter(getSupportFragmentManager(), TOTAL_STEPS);
        viewPager.setAdapter(registrationStepsAdapter);
        viewPager.setOffscreenPageLimit(TOTAL_STEPS);

        // Indicator setup..
        new ViewPagerIndicator(context, ViewPagerIndicator.STYLE_NORMAL).setup(viewPager, layoutIndicator, R.drawable.circle_black, R.drawable.circle_grey);

        // Un-Scrollable
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(INITIAL_STEP);
            }
        });
    }

    public void setName(String name) {
        registrationRequest.setFirstName(name);
    }
    public void setLastName(String lastname) {
        registrationRequest.setLastName(lastname);
    }
    public void setEmail(String email) {
        registrationRequest.setEmail(email);
    }

    public void setPass(String pass) {
        registrationRequest.setPassword(pass);
    }

    public void setGender(String gender) {
        registrationRequest.setGender(gender);
    }

    public RegistrationRequest getRegistrationRequest() {
        return registrationRequest;
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
