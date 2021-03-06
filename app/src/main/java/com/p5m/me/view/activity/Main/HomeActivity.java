package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.p5m.me.R;
import com.p5m.me.adapters.HomeAdapter;
import com.p5m.me.adapters.viewholder.ProfileHeaderTabViewHolder;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.UnratedClassData;
import com.p5m.me.data.main.BookingCancellationResponse;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.LogoutRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.firebase_dynamic_link.FirebaseDynamicLinnk;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.CommonUtillity;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.activity.custom.BottomTapLayout;
import com.p5m.me.view.custom.CustomRateAlertDialog;
import com.p5m.me.view.custom.WorkoutDurationAlert;
import com.p5m.me.view.fragment.FindClass;
import com.p5m.me.view.fragment.MembershipFragment;
import com.p5m.me.view.fragment.MySchedule;
import com.p5m.me.view.fragment.ViewPagerFragmentSelection;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_DONE;
import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_HAVE_PACKAGE;
import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_NO_PACKAGE;
import static com.p5m.me.utils.AppConstants.Tab.TAB_FIND_CLASS;
import static com.p5m.me.utils.AppConstants.Tab.TAB_MY_MEMBERSHIP;
import static com.p5m.me.utils.AppConstants.Tab.TAB_MY_PROFILE;


public class HomeActivity extends BaseActivity implements BottomTapLayout.TabListener, ViewPager.OnPageChangeListener, View.OnClickListener, NetworkCommunicator.RequestListener, TabChange {

    private static HomeAdapter homeAdapter;

    public static void open(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    public static void show(Context context, int tabPosition, int navigationFrom) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        intent.putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        context.startActivity(intent);

    }
    public static void show(Context context, int tabPosition, int navigationFrom,boolean addCredits) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        intent.putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        intent.putExtra(AppConstants.DataKey.ADD_EXTRA_CREDITS, addCredits);

        context.startActivity(intent);

    }

    public static void show(Context context, int tabPosition, int navigationFrom, ClassModel classModel) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        intent.putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        intent.putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel);
        context.startActivity(intent);

    }

    public static void show(Context context, int tabPosition, int navigationFrom, ClassModel classModel, BookWithFriendData friendData, int numberOfPackagesToBuy) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        intent.putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        intent.putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel);
        intent.putExtra(AppConstants.DataKey.BOOK_WITH_FRIEND_DATA, friendData);
        intent.putExtra(AppConstants.DataKey.NUMBER_OF_PACKAGES_TO_BUY, numberOfPackagesToBuy);
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
        intent.putExtra(AppConstants.DataKey.HOME_TABS_SCHEDULE_INNER_TAB_POSITION, innerTabPosition);
        return intent;
    }

    public static Intent createIntent(Context context, int tabPosition, int innerTabPosition,String type,String id) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        intent.putExtra(AppConstants.DataKey.HOME_TABS_SCHEDULE_INNER_TAB_POSITION, innerTabPosition);
        intent.putExtra(AppConstants.DataKey.HOME_FILTER_TYPE, type);
        intent.putExtra(AppConstants.DataKey.FILTER_ID, id);

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

    public static Intent showMembership(Context context, int tabPosition, int navigationFrom) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AppConstants.DataKey.HOME_TAB_POSITION, tabPosition);
        intent.putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        return intent;

    }

    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    @BindView(R.id.layoutBottomTabs)
    public LinearLayout layoutBottomTabs;

    @BindView(R.id.buyClassesLayout)
    public LinearLayout buyClassesLayout;

    @BindView(R.id.buyClasses)
    public TextView buyClasses;

    @BindView(R.id.availableCredit)
    public TextView availableCredit;

    private BottomTapLayout bottomTapLayout;

    private final int TOTAL_TABS = 5;
    private int INITIAL_POSITION = AppConstants.Tab.TAB_FIND_CLASS;
    private int currentTab = INITIAL_POSITION;
    private int PROFILE_TAB_POSITION;
    private int SCHEDULE_TAB_POSITION;
    private int NAVIGATED_FROM_INT = -1;
    private int NUMBER_OF_PACKAGES_TO_BUY = -1;
    private BookWithFriendData BOOK_WITH_FRIEND_DATA;
    private ClassModel CLASS_OBJECT;
    private boolean ADD_EXTRA_CREDITS;

    private Handler handler;
    public CustomRateAlertDialog mCustomMatchDialog;
    private static User.WalletDto mWalletCredit;
    private String filterType,filterId;


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
            SCHEDULE_TAB_POSITION = getIntent().getIntExtra(AppConstants.DataKey.HOME_TABS_SCHEDULE_INNER_TAB_POSITION,
                    AppConstants.Tab.TAB_MY_SCHEDULE_UPCOMING);

            NAVIGATED_FROM_INT = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT,
                    -1);
            NUMBER_OF_PACKAGES_TO_BUY = getIntent().getIntExtra(AppConstants.DataKey.NUMBER_OF_PACKAGES_TO_BUY,
                    1);
            CLASS_OBJECT = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_OBJECT);
            BOOK_WITH_FRIEND_DATA = (BookWithFriendData) getIntent().getSerializableExtra(AppConstants.DataKey.BOOK_WITH_FRIEND_DATA);
            filterType = getIntent().getStringExtra(AppConstants.DataKey.HOME_FILTER_TYPE);
            filterId = getIntent().getStringExtra(AppConstants.DataKey.FILTER_ID);



            LogUtils.debug("VarunSCHEDULE getIntent " + SCHEDULE_TAB_POSITION);

        }
        if(TempStorage.getDefaultPage()>-1){
            INITIAL_POSITION = TempStorage.getDefaultPage();
            TempStorage.setDefaultPage(-1);
        }
        RefrenceWrapper.getRefrenceWrapper(this).setActivity(this);
        buyClassesLayout.setOnClickListener(this);
        GlobalBus.getBus().register(this);
        FirebaseDynamicLinnk.getDynamicLink(this, getIntent());
        handler = new Handler(Looper.getMainLooper());
        setupBottomTabs();
        RemoteConfigure.getFirebaseRemoteConfig(context).fetchRemoteConfig();
        homeAdapter = new HomeAdapter(((BaseActivity) activity).getSupportFragmentManager(), TOTAL_TABS, PROFILE_TAB_POSITION, SCHEDULE_TAB_POSITION, NAVIGATED_FROM_INT, NUMBER_OF_PACKAGES_TO_BUY, CLASS_OBJECT, BOOK_WITH_FRIEND_DATA);
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
        setMembershipIcon();
        IntercomEvents.updateIntercom();
        try {
            List<ClassModel> classList = TempStorage.getSavedClasses();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!showAlertForGoogleFormReview()){
            openRateAlertDialog();
        }
        networkCommunicator.getActivities(this, true);
        networkCommunicator.getRatingParameters(this, true);
        checkFacebookSessionStatus();
        TempStorage.setFilterList(null);

        onTrackingNotification();
        networkCommunicator.getMyUser(this, false);
        handleMembershipInfoState(TempStorage.getUser());
        networkCommunicator.getCancellationReason(this, false);

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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallDisconnected(Events.OnUserDisconnectedCall onUserDisconnectedCall){
    }

    private void setNotificationIcon() {
        bottomTapLayout.getTabViewNotification(AppConstants.Tab.TAB_SCHEDULE, MyPreferences.initialize(context).getNotificationCount());
    }

    private void setMembershipIcon() {
        bottomTapLayout.getTabViewMembership(TAB_MY_MEMBERSHIP, MyPreferences.initialize(context).getMembershipIcon());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        INITIAL_POSITION = intent.getIntExtra(AppConstants.DataKey.HOME_TAB_POSITION,
                AppConstants.Tab.TAB_FIND_CLASS);

        PROFILE_TAB_POSITION = intent.getIntExtra(AppConstants.DataKey.HOME_TABS_PROFILE_INNER_TAB_POSITION,
                ProfileHeaderTabViewHolder.TAB_1);

        SCHEDULE_TAB_POSITION = intent.getIntExtra(AppConstants.DataKey.HOME_TABS_SCHEDULE_INNER_TAB_POSITION,
                AppConstants.Tab.TAB_MY_SCHEDULE_UPCOMING);

        NAVIGATED_FROM_INT = intent.getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT,
                -1);
        NUMBER_OF_PACKAGES_TO_BUY = intent.getIntExtra(AppConstants.DataKey.NUMBER_OF_PACKAGES_TO_BUY,
                1);
        ADD_EXTRA_CREDITS = intent.getBooleanExtra(AppConstants.DataKey.ADD_EXTRA_CREDITS,false);
        CLASS_OBJECT = (ClassModel) intent.getSerializableExtra(AppConstants.DataKey.CLASS_OBJECT);
        BOOK_WITH_FRIEND_DATA = (BookWithFriendData) intent.getSerializableExtra(AppConstants.DataKey.BOOK_WITH_FRIEND_DATA);


        LogUtils.debug("VarunSCHEDULE getNewIntent " + SCHEDULE_TAB_POSITION);

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

        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_EXPLORE_PAGE, R.drawable.explore, R.drawable.explore,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.explore_page), context.getString(R.string.explore_page)));

        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_SCHEDULE, R.drawable.schedule, R.drawable.schedule,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.schedule), context.getString(R.string.schedule)));

        tabList.add(new BottomTapLayout.Tab(AppConstants.Tab.TAB_MY_MEMBERSHIP, R.drawable.membership_tab_icon, R.drawable.membership_tab_icon,
                ContextCompat.getColor(context, R.color.theme_accent_text), ContextCompat.getColor(context, R.color.theme_light_text),
                context.getString(R.string.membership), context.getString(R.string.membership)));

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
        try {
            ((ViewPagerFragmentSelection) homeAdapter.getFragments().get(position)).onTabSelection(position);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        bottomTapLayout.setTab(position);

        if (position == AppConstants.Tab.TAB_FIND_CLASS) {
            handleBuyClassesButton();
            handleTabChangeForFindClass(position);
        } else {
            buyClassesLayout.setVisibility(View.GONE);
        }

        if (position == AppConstants.Tab.TAB_SCHEDULE) {
            MixPanel.trackScheduleVisit();
            try {
                ((MySchedule) homeAdapter.getFragments().get(position)).selectTab(SCHEDULE_TAB_POSITION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(position == TAB_FIND_CLASS){
            MixPanel.trackFindClassVisit();

        }
        if(position == TAB_MY_PROFILE){
            MixPanel.trackProfileVisit();

        }
        if(position == AppConstants.Tab.TAB_EXPLORE_PAGE){
            MixPanel.trackExploreVisit();

        }if(position == TAB_MY_MEMBERSHIP){
            MixPanel.trackMembershipVisit(NAVIGATED_FROM_INT);
            FirebaseAnalysic.trackMembershipVisit(this.NAVIGATED_FROM_INT);
            IntercomEvents.trackMembershipVisit(this.NAVIGATED_FROM_INT);
        }

        handleTabChangeForMembership(position);
        currentTab = position;
    }

    private void handleTabChangeForFindClass(int position) {
        if (position == TAB_FIND_CLASS && currentTab != position) {
            try {
                FindClass fragment = ((FindClass) homeAdapter.getFragments().get(position));
                fragment.refreshFragment(NAVIGATED_FROM_INT);

            } catch (Exception e) {
                e.printStackTrace();
            }
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
                buyClassesLayout.setVisibility(View.VISIBLE);
//                UpdateBuyClassText update = new UpdateBuyClassText();
//                update.execute();
            } else {
                buyClassesLayout.setVisibility(View.GONE);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onTabChange(int initial_position, int schedule_tab_position) {
        homeAdapter = new HomeAdapter(((BaseActivity) activity).getSupportFragmentManager(), TOTAL_TABS, 2, schedule_tab_position);
        viewPager.setAdapter(homeAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(TOTAL_TABS);

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(initial_position);
            }
        });

    }


    private class UpdateBuyClassText extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... url) {
            User user = TempStorage.getUser();
            mWalletCredit = user.getWalletDto();
            if (mWalletCredit != null && mWalletCredit.getBalance() > 0) {
                return context.getResources().getString(R.string.wallet_text) + " : " + LanguageUtils.numberConverter(mWalletCredit.getBalance(), 2) + " " + TempStorage.getUser().getCurrencyCode();
            } else {
                return "";

            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && result.length() > 0) {
                availableCredit.setVisibility(View.VISIBLE);

                availableCredit.setText(result);

            } else {
                availableCredit.setVisibility(View.GONE);

            }


        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        buyClasses.setText(RemoteConfigConst.BUY_CLASS_VALUE);
        RemoteConfigSetUp.setBackgroundColor(buyClassesLayout, RemoteConfigConst.BUY_CLASS_COLOR_VALUE, context.getResources().getColor(R.color.theme_book));
        FirebaseAnalysic.viewHomePage();
        if (currentTab == AppConstants.Tab.TAB_FIND_CLASS) {
            handleBuyClassesButton();
            buyClassesLayout.setVisibility(View.GONE);
        } else {
            buyClassesLayout.setVisibility(View.GONE);

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
            case R.id.buyClassesLayout: {
                // MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS);
                HomeActivity.show(context, TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS);

            }
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
            case NetworkCommunicator.RequestCode.GET_CANCELLATION_REASON:
                List<BookingCancellationResponse> reasonsModel = ((ResponseModel<List<BookingCancellationResponse>>) response).data;
                TempStorage.setCancellationReasons(reasonsModel);
                break;

        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.RATING_PARAMS:

                break;
            case NetworkCommunicator.RequestCode.GET_CANCELLATION_REASON:
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


    private void handleTabChangeForMembership(int position) {
        if (position == TAB_MY_MEMBERSHIP && currentTab != position) {
            try {
                MyPreferences.getInstance().saveMembershipIcon(false);
                setMembershipIcon();
                MembershipFragment fragment = ((MembershipFragment) homeAdapter.getFragments().get(position));
                fragment.refreshFragment(NAVIGATED_FROM_INT, CLASS_OBJECT, BOOK_WITH_FRIEND_DATA, NUMBER_OF_PACKAGES_TO_BUY,ADD_EXTRA_CREDITS);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (position != TAB_MY_MEMBERSHIP) {
            try {
                MembershipFragment fragment = ((MembershipFragment) homeAdapter.getFragments().get(TAB_MY_MEMBERSHIP));
                NAVIGATED_FROM_INT = -1;
                CLASS_OBJECT = null;
                BOOK_WITH_FRIEND_DATA = null;
                NUMBER_OF_PACKAGES_TO_BUY = 1;
                ADD_EXTRA_CREDITS = false;
                fragment.refreshFragmentBackGroung(NAVIGATED_FROM_INT, CLASS_OBJECT, BOOK_WITH_FRIEND_DATA, NUMBER_OF_PACKAGES_TO_BUY);
                    fragment.fragmentPaused();


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (position == TAB_MY_MEMBERSHIP && currentTab == position && NAVIGATED_FROM_INT == AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS) {
            try {
                MembershipFragment fragment = ((MembershipFragment) homeAdapter.getFragments().get(TAB_MY_MEMBERSHIP));
                fragment.refreshFragment(NAVIGATED_FROM_INT, CLASS_OBJECT, BOOK_WITH_FRIEND_DATA, NUMBER_OF_PACKAGES_TO_BUY,ADD_EXTRA_CREDITS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMembershipInfoState(User user) {
        if (TempStorage.isOpenMembershipInfo() != MEMBERSHIP_INFO_STATE_DONE) {
            if (user.isBuyMembership()) {
                TempStorage.setOpenMembershipInfo(MEMBERSHIP_INFO_STATE_NO_PACKAGE);

            } else {
                TempStorage.setOpenMembershipInfo(MEMBERSHIP_INFO_STATE_HAVE_PACKAGE);

            }
        }

    }
    private boolean showAlertForGoogleFormReview(){
        ClassModel classModel;
        try{
            classModel = TempStorage.getClassForGoogleFormReview();
            if(classModel!=null){
                long timeInClass = TempStorage.getUserTimeInSession(classModel.getClassSessionId());
                double timePercentage =  DateUtils.getClassCompletionPercentage(classModel,timeInClass);
                String googleFormUrl;
                if(timeInClass>0&&timePercentage>75){
                    googleFormUrl = RemoteConfigure.getFirebaseRemoteConfig(mContext).getRemoteConfigValue(RemoteConfigConst.FINISHED_GOOGLE_FORM);
                    String durationString = CommonUtillity.formatDuration(timeInClass*1000);
                    String message = RemoteConfigure.getFirebaseRemoteConfig(mContext).getRemoteConfigValue(RemoteConfigConst.CALL_FINISH_MESSAGE);
                    String title =  RemoteConfigure.getFirebaseRemoteConfig(mContext).getRemoteConfigValue(RemoteConfigConst.CALL_FINISHED_TITLE);;
                    String buttonTitle =RemoteConfigure.getFirebaseRemoteConfig(mContext).getRemoteConfigValue(RemoteConfigConst.CALL_FINISH_BUTTON_TITLE);

                    WorkoutDurationAlert dialog =  new WorkoutDurationAlert(mContext,title,message,buttonTitle,googleFormUrl);
                    dialog.show();
                    dialog.setOnDismissListener(dialog1 -> openRateAlertDialog());
                }
                 TempStorage.clearClassForGoogleFormReview();
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return classModel!=null;
    }
}


interface TabChange {
    void onTabChange(int initial_position, int schedule_tab_position);
}


