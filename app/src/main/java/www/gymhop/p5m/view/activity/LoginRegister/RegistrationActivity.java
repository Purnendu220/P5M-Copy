package www.gymhop.p5m.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.RegistrationStepsAdapter;
import www.gymhop.p5m.data.request.RegistrationRequest;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.ViewPagerIndicator;
import www.gymhop.p5m.view.activity.base.BaseActivity;

/**
 * Created by bittu on 31/8/17.
 */

public class RegistrationActivity extends BaseActivity {

    public static void open(Context context) {
        context.startActivity(new Intent(context, RegistrationActivity.class));
    }

    @BindView(R.id.imageViewBack)
    public ImageView imageViewBack;
    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.layoutIndicator)
    public LinearLayout layoutIndicator;

    private int TOTAL_STEPS = 4;
    private RegistrationStepsAdapter registrationStepsAdapter;

    private RegistrationRequest registrationRequest;

    @Override
    public void onCreate(Bundle bundle) {
        showActionBar = false;
        super.onCreate(bundle);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(activity);

        registrationRequest = new RegistrationRequest();
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
    }

    public void setName(String name) {
        registrationRequest.setFirstName(name);
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
        if (viewPager.getCurrentItem() != AppConstants.FragmentPosition.REGISTRATION_STEP_GENDER) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        if (viewPager.getCurrentItem() != AppConstants.FragmentPosition.REGISTRATION_STEP_NAME) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != AppConstants.FragmentPosition.REGISTRATION_STEP_NAME) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            super.onBackPressed();
        }
    }
}
