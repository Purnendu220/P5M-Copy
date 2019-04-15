package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.p5m.me.R;
import com.p5m.me.adapters.HomeAdapter;
import com.p5m.me.adapters.viewholder.ProfileHeaderTabViewHolder;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.PushDetailModel;
import com.p5m.me.data.UnratedClassData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.DefaultSettingServer;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.LogoutRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.helper.ClassListListenerHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.activity.custom.BottomTapLayout;
import com.p5m.me.view.custom.CustomAlertDialog;
import com.p5m.me.view.custom.CustomRateAlertDialog;
import com.p5m.me.view.fragment.ViewPagerFragmentSelection;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class HomeActivity extends BaseActivity implements BottomTapLayout.TabListener, ViewPager.OnPageChangeListener, View.OnClickListener, NetworkCommunicator.RequestListener {

    public static void open(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void show(Context context, int tabPosition) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        context.startActivity(intent);
    }

    public static Intent createIntent(Context context, int tabPosition, int innerTabPosition) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        intent.putExtra(AppConstants.DataKey.HOME_TABS_INNER_TAB_POSITION, innerTabPosition);
        return intent;
    }

    public static Intent createIntent(Context context, int tabPosition, int innerTabPosition, int profileTabPosition) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        intent.putExtra(AppConstants.DataKey.HOME_TABS_INNER_TAB_POSITION, innerTabPosition);
        intent.putExtra(AppConstants.DataKey.HOME_TABS_PROFILE_INNER_TAB_POSITION, profileTabPosition);


        return intent;
    }

    public static Intent createIntent(Context context, int tabPosition, int innerTabPosition, ClassModel model) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        intent.putExtra(AppConstants.DataKey.HOME_TABS_INNER_TAB_POSITION, innerTabPosition);
        intent.putExtra(AppConstants.DataKey.CLASS_MODEL, model);
        return intent;
    }

    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    @BindView(R.id.layoutBottomTabs)
    public LinearLayout layoutBottomTabs;

    @BindView(R.id.buyClasses)
    public Button buyClasses;

    private BottomTapLayout bottomTapLayout;
    private HomeAdapter homeAdapter;

    private final int TOTAL_TABS = 4;
    private int INITIAL_POSITION = AppConstants.Tab.TAB_FIND_CLASS;
    private int currentTab = INITIAL_POSITION;
    private int PROFILE_TAB_POSITION;

    private Handler handler;
    public CustomRateAlertDialog mCustomMatchDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // If coming from a shared link, then open that link after login..
        if (DeepLinkActivity.url != null) {
            overridePendingTransition(0, 0);
            DeepLinkActivity.open(context, DeepLinkActivity.url);
            finish();
            return;
        }

        ButterKnife.bind(activity);
        if (getIntent() != null) {
            INITIAL_POSITION = getIntent().getIntExtra(AppConstants.DataKey.HOME_TAB_POSITION,
                    AppConstants.Tab.TAB_FIND_CLASS);
            PROFILE_TAB_POSITION = getIntent().getIntExtra(AppConstants.DataKey.HOME_TABS_PROFILE_INNER_TAB_POSITION,
                    ProfileHeaderTabViewHolder.TAB_1);
        }
        RefrenceWrapper.getRefrenceWrapper(this).setActivity(this);
        buyClasses.setOnClickListener(this);
        GlobalBus.getBus().register(this);

        handler = new Handler(Looper.getMainLooper());
        setupBottomTabs();

        homeAdapter = new HomeAdapter(((BaseActivity) activity).getSupportFragmentManager(), TOTAL_TABS, PROFILE_TAB_POSITION);
        viewPager.setAdapter(homeAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(TOTAL_TABS);

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(INITIAL_POSITION);
            }
        });

        setNotificationIcon();
        try {
            List<ClassModel> classList = TempStorage.getSavedClasses();
        } catch (Exception e) {
            e.printStackTrace();
        }
        openRateAlertDialog();

        networkCommunicator.getRatingParameters(this, true);
        checkFacebookSessionStatus();

        onTrackingNotification();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notificationReceived(Events.NotificationCountUpdated notificationCountUpdated) {
        setNotificationIcon();
    }

    private void setNotificationIcon() {
        bottomTapLayout.getTabViewNotification(AppConstants.Tab.TAB_SCHEDULE, MyPreferences.initialize(context).getNotificationCount());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        INITIAL_POSITION = intent.getIntExtra(AppConstants.DataKey.HOME_TAB_POSITION,
                AppConstants.Tab.TAB_FIND_CLASS);

        LogUtils.debug("Home screen onNewIntent " + INITIAL_POSITION);

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(INITIAL_POSITION);
            }
        });

    }

    private void setupBottomTabs() {
        List<BottomTapLayout.Tab> tabList = new ArrayList<>();
        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_FIND_CLASS, R.drawable.find_a_class, R.drawable.find_a_class,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.find_class), context.getString(R.string.find_class)));
        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_TRAINER, R.drawable.trainers, R.drawable.trainers,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.trainer), context.getString(R.string.trainers)));
        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_SCHEDULE, R.drawable.schedule, R.drawable.schedule,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.schedule), context.getString(R.string.schedule)));
        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_MY_PROFILE, R.drawable.profile, R.drawable.profile,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.profile), context.getString(R.string.profile)));

        bottomTapLayout = new BottomTapLayout();
        bottomTapLayout.setup(context, layoutBottomTabs, tabList, this);

        bottomTapLayout.setTab(AppConstants.Tab.TAB_FIND_CLASS);
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
        currentTab = position;
        try {
            ((ViewPagerFragmentSelection) homeAdapter.getFragments().get(position)).onTabSelection(position);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        bottomTapLayout.setTab(position);
        if (position == AppConstants.Tab.TAB_FIND_CLASS) {
//            handleApptimize();
            handleBuyClassesButton();
        } else {
            buyClasses.setVisibility(View.GONE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private boolean isBackRequested;

    @Override
    public void onBackPressed() {
        if (!isBackRequested) {
            isBackRequested = true;
            ToastUtils.show(context, getString(R.string.press_again_to_exit));
        } else {
            super.onBackPressed();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackRequested = false;
            }
        }, 2000);
    }

    private void handleBuyClassesButton() {
        try {
            User user = TempStorage.getUser();
            if (user.isBuyMembership()) {
                buyClasses.setVisibility(View.VISIBLE);

            } else {
                buyClasses.setVisibility(View.GONE);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        buyClasses.setText(RemoteConfigConst.BUY_CLASS_VALUE);
        RemoteConfigSetUp.setBackgroundColor(buyClasses, RemoteConfigConst.BUY_CLASS_COLOR_VALUE, context.getResources().getColor(R.color.theme_book));

        if (currentTab == AppConstants.Tab.TAB_FIND_CLASS) {
            handleBuyClassesButton();
        } else {
            buyClasses.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCustomMatchDialog != null && mCustomMatchDialog.isShowing()) {
            mCustomMatchDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buyClasses: {
                MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS);
            }
            break;
        }

    }

    private void openRateAlertDialog() {
        ClassModel model;
        model = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_MODEL);
        if (model != null) {
            showAlert(model);
        } else {
            networkCommunicator.getUnratedClassList(0, 1, this, true);
        }
    }

    private void showAlert(ClassModel model) {
        if (currentTab == AppConstants.Tab.TAB_FIND_CLASS) {
            mCustomMatchDialog = new CustomRateAlertDialog(this, model, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS);
            try {
                mCustomMatchDialog.show();
                refrenceWrapper.setCustomRateAlertDialog(mCustomMatchDialog);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.RATING_PARAMS:

                break;
            case NetworkCommunicator.RequestCode.UNRATED_CLASS_COUNT:
                UnratedClassData classModels = ((ResponseModel<UnratedClassData>) response).data;
                if (classModels != null && classModels.getClassDetailList() != null && classModels.getClassDetailList().size() > 0) {
                    showAlert(classModels.getClassDetailList().get(0));
                }

                break;
            case NetworkCommunicator.RequestCode.LOGOUT:
                EventBroadcastHelper.logout(context);
                break;

        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.RATING_PARAMS:

                break;
            case NetworkCommunicator.RequestCode.UNRATED_CLASS_COUNT:
                break;

            case NetworkCommunicator.RequestCode.LOGOUT:
                EventBroadcastHelper.logout(context);
                break;


        }
    }

    private void checkFacebookSessionStatus() {
        try {
            if (MyPreferences.getInstance().isLoginWithFacebook()) {
                if (AccessToken.getCurrentAccessToken() != null && AccessToken.getCurrentAccessToken().getToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
                    makeGraphRequest();

                } else {
                    showFacebookSessionExpiredDialog();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void makeGraphRequest() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {
                            showFacebookSessionExpiredDialog();
                            return;
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,birthday,gender,email,location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void showFacebookSessionExpiredDialog() {
        DialogUtils.showBasicMessageCancelableFalse(context, "Your facebook session is expired.Please login again.", context.getResources().getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                networkCommunicator.logout(new LogoutRequest(TempStorage.getUser().getId()), HomeActivity.this, false);
            }
        });
    }


    public void navigateToMyProfile() {
        RefrenceWrapper.getRefrenceWrapper(this).setMyProfileTabPosition(ProfileHeaderTabViewHolder.TAB_2);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(AppConstants.Tab.TAB_MY_PROFILE);
            }
        });
    }


}
