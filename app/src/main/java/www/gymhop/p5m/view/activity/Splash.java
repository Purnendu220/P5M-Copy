package www.gymhop.p5m.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.storage.preferences.MyPreferences;
import www.gymhop.p5m.view.activity.LoginRegister.InfoScreen;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class Splash extends BaseActivity {

    private Handler handler;
    private Runnable nextScreenRunnable;
    private long DELAY_NAVIGATION = 1400; // 1.4 sec

    @BindView(R.id.imageViewImage)
    public ImageView imageViewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(activity);

        handler = new Handler();

        imageViewImage.animate().scaleXBy(0.3f).scaleYBy(0.3f).setDuration(1000).setInterpolator(new BounceInterpolator()).start();

        startTimerForGoToNextScreen();
    }

    private void startTimerForGoToNextScreen() {
        nextScreenRunnable = new Runnable() {
            @Override
            public void run() {

                if(MyPreferences.getInstance(context).isLogin()) {
                    /////////// Home Screen ////////////


                } else {
                    /////////// Login Page ////////////
                    InfoScreen.openActivity(context);
                }

                finish();
            }
        };

        handler.postDelayed(nextScreenRunnable, DELAY_NAVIGATION);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(nextScreenRunnable);
    }
}
