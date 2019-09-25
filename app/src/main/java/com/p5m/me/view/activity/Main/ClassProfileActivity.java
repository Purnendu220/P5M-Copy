package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ClassProfileAdapter;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.ClassRatingUserData;
import com.p5m.me.data.Join5MinModel;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.WishListResponse;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.data.request.JoinClassRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.firebase_dynamic_link.FirebaseDynamicLinnk;
import com.p5m.me.helper.ClassListListenerHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.CalendarHelper;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.BookForAFriendPopup;
import com.p5m.me.view.custom.CustomAlertDialog;
import com.p5m.me.view.fragment.BottomSheetClassBookingOptions;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.p5m.me.utils.AppConstants.Limit.PAGE_LIMIT_MAIN_CLASS_LIST;

public class ClassProfileActivity extends BaseActivity implements AdapterCallbacks, View.OnClickListener, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener, CustomAlertDialog.OnAlertButtonAction {

    private String message;
    private int errorMsg;

    public static void open(Context context, ClassModel classModel, int navigationFrom) {
        context.startActivity(new Intent(context, ClassProfileActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel));
    }

    public static void open(Context context, int classId, int navigationFrom) {
        context.startActivity(new Intent(context, ClassProfileActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstants.DataKey.CLASS_SESSION_ID_INT, classId));
    }

    public static Intent createIntent(Context context, int classId, int navigationFrom) {
        return new Intent(context, ClassProfileActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstants.DataKey.CLASS_SESSION_ID_INT, classId)
                .putExtra(AppConstants.DataKey.IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN, true);

    }

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.layoutBottomTabs)
    public LinearLayout layoutBottomTabs;

    @BindView(R.id.layoutButton)
    public View layoutButton;
    @BindView(R.id.textViewBook)
    public TextView textViewBook;
    @BindView(R.id.textViewBookWithFriend)
    public TextView textViewBookWithFriend;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    private User user;
    public View imageViewOptions;
    private ClassProfileAdapter classProfileAdapter;
    private ClassModel classModel;
    private Join5MinModel join5MinModel;
    private int classSessionId;
    private int page;
    private boolean isNavigationFromSharing;
    private int navigationFrom;
    ClassRatingUserData ratingData;
    private boolean isBookWithFriendInProgress = false;
    private BookWithFriendData mBookWithFriendData;
    private Hashtable<String, String> calendarIdTable;
    private int calendar_id = -1;
    private TextView mTextViewWalletAmount;
    private LinearLayout mLayoutUserWallet;
    private static User.WalletDto mWalletCredit;


    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void classJoin(Events.ClassJoin data) {
        handleClassJoined(data.data);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void packagePurchasedForClass(Events.PackagePurchasedForClass data) {
        handleClassJoined(data.data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bookwithFriend(Events.BookWithFriend data) {
        isBookWithFriendInProgress = true;
        mBookWithFriendData = data.friendData;
        bookWithAFriend(data.friendData);
        textViewBookWithFriend.setText(getText(R.string.please_wait));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void classPurchased(Events.ClassPurchased data) {
        handleClassJoined(data.data);
    }

    private void handleClassJoined(ClassModel data) {
        try {
            classModel.setUserJoinStatus(data.isUserJoinStatus());
            try {
                classProfileAdapter.getClassModel().setAvailableSeat(data.getAvailableSeat());
                classProfileAdapter.notifyItemChanged(0);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
            Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_profile);
        ButterKnife.bind(activity);
        GlobalBus.getBus().register(this);
        FirebaseDynamicLinnk.getDynamicLink(this, getIntent());
        classModel = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_OBJECT);
        classSessionId = getIntent().getIntExtra(AppConstants.DataKey.CLASS_SESSION_ID_INT, -1);
        navigationFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);
        textViewBook.setEnabled(true);
        join5MinModel = new Join5MinModel();
        if (classModel == null && classSessionId == -1) {
            finish();
            return;
        }
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);

        classProfileAdapter = new ClassProfileAdapter(context, AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE, this);
        StickyLayoutManager layoutManager = new StickyLayoutManager(context, classProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(classProfileAdapter);
        message = activity.getString(R.string.non_refundable_warning_msg);

        if (classModel != null) {
            classSessionId = classModel.getClassSessionId();
            classProfileAdapter.setClass(classModel);
            classProfileAdapter.notifyDataSetChanged();
            IntercomEvents.trackClassVisit(classModel.getTitle());
            Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);
        } else {
            layoutButton.setVisibility(View.VISIBLE);
        }
        user = TempStorage.getUser();
        onRefresh();

        try {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        setToolBar();

        getDynamicLink();
        MixPanel.trackClassDetails();
        onTrackingNotification();


//        networkCommunicator.getMyUser(this, false);

    }

    private void getDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }

                        if (deepLink != null) {
                            Log.d("Deep Link", "Found deep link ClassProfile!");

                        } else {
                            Log.d("Deep Link", "getDynamicLink: no link found");

                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Deep Link", "getDynamicLink:onFailure", e);

                    }
                });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        networkCommunicator.getClassDetail(classSessionId, this, false);

    }

    @OnClick(R.id.textViewBook)
    public void textViewBook() {
        isBookWithFriendInProgress = false;
        mBookWithFriendData = null;
        // Check if class is allowed for the gender..
        if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)
                && !Helper.isMalesAllowed(classModel)) {
            ToastUtils.show(context, context.getString(R.string.gender_females_only_error));
            return;
        } else if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)
                && !Helper.isFemalesAllowed(classModel)) {
            ToastUtils.show(context, context.getString(R.string.gender_males_only_error));
            return;
        }
        textViewBook.setEnabled(false);

        textViewBook.setText(context.getResources().getString(R.string.please_wait));
        if (classModel.getAvailableSeat() == 0 && classModel.isUserJoinStatus() == false) {
            networkCommunicator.addToWishList(classModel, classModel.getClassSessionId(), new NetworkCommunicator.RequestListener() {
                @Override
                public void onApiSuccess(Object response, int requestCode) {
                    textViewBook.setEnabled(false);
                    try {
                        if (classModel.getAvailableSeat() == 0 && classModel.isUserJoinStatus() == false) {
                            String message = String.format(context.getString(R.string.added_to_waitlist));
                            DialogUtils.showBasicMessage(context, message, context.getString(R.string.view_wishlist), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_SCHEDULE, AppConstants.Tab.TAB_MY_SCHEDULE_WISH_LIST);
                                    context.startActivity(navigationIntent);
                                    textViewBook.setEnabled(true);
                                }
                            });
                        } else {
                            String message = String.format(context.getString(R.string.added_to_wishlist), classModel.getTitle());
                            ToastUtils.show(context, message);
                        }
                        classModel.setWishListId(((ResponseModel<WishListResponse>) response).data.getId());
                        classModel.setWishType(AppConstants.ApiParamKey.WAITLIST);
                        EventBroadcastHelper.sendWishAdded(classModel);
                        EventBroadcastHelper.waitlistClassJoin(context, classModel);
                        Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);


                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.exception(e);
                    }

                }

                @Override
                public void onApiFailure(String errorMessage, int requestCode) {
                    textViewBook.setEnabled(true);

                    ToastUtils.show(context, errorMessage);
                    Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);

                }
            });
            return;
        }


        if (Helper.isSpecialClass(classModel)) {
            if (Helper.isFreeClass(classModel))
                joinClass();
            else {
                if (!user.isBuyMembership())
                    CheckoutActivity.openActivity(context, classModel, 1);

                else {
                    if (Helper.isSpecialClass(classModel) &&
                            !Helper.isFreeClass(classModel)
                    ) {

                        CheckoutActivity.openActivity(context, classModel, 1);

                    } else {
                        joinClass();

                    }
                }
            }
            return;
        }


        networkCommunicator.getMyUser(new NetworkCommunicator.RequestListener() {
            @Override
            public void onApiSuccess(Object response, int requestCode) {
                performJoinProcess();

//                warningNonRefundablePopUp();
                Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);
            }

            @Override
            public void onApiFailure(String errorMessage, int requestCode) {
                ToastUtils.show(context, errorMessage);
                Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);
            }
        }, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void waitlistJoin(Events.WaitlistJoin data) {
        textViewBook.setText(RemoteConfigConst.WAITLISTED_VALUE);
        RemoteConfigSetUp.setBackgroundColor(textViewBook, RemoteConfigConst.BOOKED_COLOR_VALUE, context.getResources().getColor(R.color.theme_booked));

    }

    @OnClick(R.id.textViewBookWithFriend)
    public void textViewBookWithFriend() {

        BookForAFriendPopup mBookForAFriendPopup = new BookForAFriendPopup(this, classModel, navigationFrom);
        try {
            mBookForAFriendPopup.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void bookWithAFriend(final BookWithFriendData data) {

        // Check if class is allowed for the gender..
        if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)
                && !Helper.isMalesAllowed(classModel)) {
            ToastUtils.show(context, mContext.getResources().getText(R.string.gender_females_only_error).toString());
            return;
        } else if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)
                && !Helper.isFemalesAllowed(classModel)) {
            ToastUtils.show(context, mContext.getResources().getText(R.string.gender_males_only_error).toString());
            return;
        }
        if (data.getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)
                && !Helper.isMalesAllowed(classModel)) {
            ToastUtils.show(context, mContext.getResources().getText(R.string.gender_females_only_error).toString());
            return;
        } else if (data.getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)
                && !Helper.isFemalesAllowed(classModel)) {
            ToastUtils.show(context, mContext.getResources().getText(R.string.gender_males_only_error).toString());
            return;
        }


        if (Helper.isSpecialClass(classModel)) {
            if (Helper.isFreeClass(classModel))
                joinClassWithFriend(data);
            else {
                CheckoutActivity.openActivity(context, classModel, 2, data);
            }
            return;
        }

        textViewBookWithFriend.setText(context.getResources().getString(R.string.please_wait));
        textViewBookWithFriend.setEnabled(false);

        networkCommunicator.getMyUser(new NetworkCommunicator.RequestListener() {
            @Override
            public void onApiSuccess(Object response, int requestCode) {

                performJoinProcessWithFriend(data);
                Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);
            }

            @Override
            public void onApiFailure(String errorMessage, int requestCode) {
                ToastUtils.show(context, errorMessage);
                Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);
            }
        }, false);
    }

    private void performJoinProcessWithFriend(BookWithFriendData dataFriend) {
        UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());
        // if (userPackageInfo.havePackages) {
        joinClassWithFriend(dataFriend);
//        } else {
//            // 3rt condition : have no packages..
//            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel, mBookWithFriendData, 2);
//        }
    }

    private void joinClassWithFriend(BookWithFriendData data) {
        List<BookWithFriendData> friendList = new ArrayList<>();
        friendList.add(data);
        textViewBookWithFriend.setText(context.getResources().getString(R.string.please_wait));
        textViewBookWithFriend.setEnabled(false);
        networkCommunicator.joinClass(new JoinClassRequest(TempStorage.getUser().getId(), classModel.getClassSessionId(), friendList), this, false);
    }

    private void performJoinProcess() {
        boolean userHaveDropinForClass = false;
        boolean userHaveExpiredDropinForClass = false;
        boolean userHaveExpiredGeneralPackageForClass = false;


        boolean userHaveGeneralPackageForClass = false;
        ArrayList<Integer> list = new ArrayList<>();
        UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());

        if (userPackageInfo.havePackages) {

            // 1st condition : have drop-in for class..
            if (userPackageInfo.haveDropInPackage && classModel.getGymBranchDetail() != null) {
                userHaveDropinForClass = false;
                for (UserPackage userPackage : userPackageInfo.userPackageReady) {
                    if ((userPackage.getGymId() == classModel.getGymBranchDetail().getGymId()) && (userPackage.getExpiryDate() == null || DateUtils.canJoinClass(classModel.getClassDate(), userPackage.getExpiryDate()) >= 0)) {
                        userHaveDropinForClass = true;
                        list.add(userPackage.getGymId());
                    }
                    if ((userPackage.getGymId() == classModel.getGymBranchDetail().getGymId()) && (userPackage.getExpiryDate() != null && DateUtils.canJoinClass(classModel.getClassDate(), userPackage.getExpiryDate()) < 0)) {
                        userHaveExpiredDropinForClass = true;
                    }
                }

            }
            // 2st condition : have class remaining in package..
            if (userPackageInfo.haveGeneralPackage) {
                if (userPackageInfo.userPackageGeneral != null) {


                    if (DateUtils.canJoinClass(classModel.getClassDate(), userPackageInfo.userPackageGeneral.getExpiryDate()) >= 0) {
                        userHaveGeneralPackageForClass = true;

                    }
                    if (DateUtils.canJoinClass(classModel.getClassDate(), userPackageInfo.userPackageGeneral.getExpiryDate()) < 0) {
                        userHaveExpiredGeneralPackageForClass = true;

                    }

                }
            }
            try {
                if (userHaveDropinForClass || userHaveGeneralPackageForClass) {
                    joinClass();
                    return;
                } else {
                    if (userHaveExpiredGeneralPackageForClass) {
                        textViewBook.setEnabled(true);
                        DialogUtils.showBasic(context, getString(R.string.join_fail_date_expire), getString(R.string.purchase), new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();

                                joinClass();

                            }
                        });
                        return;
                    } else {
                        joinClass();
                        return;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                joinClass();
                return;
            }
        } else {
            // 3rt condition : have no packages..
            callPackageListApi(1);
        }
    }

    private void joinClass() {
        textViewBook.setText(context.getResources().getString(R.string.please_wait));
        textViewBook.setEnabled(false);
        networkCommunicator.joinClass(new JoinClassRequest(TempStorage.getUser().getId(), classModel.getClassSessionId()), this, false);
    }


    private void setToolBar() {

        BaseActivity activity = (BaseActivity) this.activity;
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_class_profile, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(this);
        imageViewOptions = v.findViewById(R.id.imageViewOptions);
        mTextViewWalletAmount = v.findViewById(R.id.textViewWalletAmount);
        mLayoutUserWallet = v.findViewById(R.id.layoutUserWallet);
        mLayoutUserWallet.setOnClickListener(this);
        imageViewOptions.setOnClickListener(this);
        ((TextView) (v.findViewById(R.id.textViewTitle))).setText(RemoteConfigConst.CLASS_CARD_TEXT_VALUE);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.imageViewClass:
                if (classModel.getClassMedia() != null) {
                    Helper.openImageViewer(context, activity, view, classModel.getClassMedia().getMediaUrl(), AppConstants.ImageViewHolderType.CLASS_IMAGE_HOLDER);
                }
                break;
            case R.id.layoutTrainer:
                if (model instanceof ClassModel) {
                    ClassModel data = (ClassModel) model;
                    if (data.getTrainerDetail() != null) {
                        TrainerProfileActivity.open(context, data.getTrainerDetail(), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);
                    } else {
                        GymProfileActivity.open(context, data.getGymBranchDetail().getGymId(), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);
                    }
                }
                break;
            case R.id.textViewLocation:
            case R.id.layoutLocation:
                if (model instanceof ClassModel) {
                    GymProfileActivity.open(context, ((ClassModel) model).getGymBranchDetail().getGymId(), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);
                }
                break;

            case R.id.imageViewMap:
            case R.id.layoutMapClick:
                if (model instanceof ClassModel) {

                    ClassModel data = (ClassModel) model;

                    String label = "";
                    try {
                        label = data.getGymBranchDetail().getBranchName();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Helper.openMap(context, data.getGymBranchDetail().getLatitude(), data.getGymBranchDetail().getLongitude(), label);
                }
                break;
            case R.id.layoutSeeAllReview:
                if (model instanceof ClassModel) {
                    ViewClassRating.open(context, ((ClassModel) model), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);
                }
                break;
        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
    }

    @Override
    public void onShowLastItem() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                onBackPressed();
                break;
            case R.id.imageViewOptions:
                if (classModel.isUserJoinStatus()) {
                    ClassListListenerHelper classListListenerHelper = new ClassListListenerHelper(context, activity, AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING, this);

                    if (classModel.getRefBookingId() != null && classModel.getRefBookingId() > 0) {
                        classListListenerHelper.popupOptionsCancelClassBookedWithFriend(context, ((BaseActivity) activity).networkCommunicator, view, classModel);

                    } else {
                        classListListenerHelper.popupOptionsCancelClass(context, ((BaseActivity) activity).networkCommunicator, view, classModel, true);

                    }
                } else
                    ClassListListenerHelper.popupOptionsAdd(context, networkCommunicator, view, classModel, navigationFrom, null);
                break;

            case R.id.layoutUserWallet:
                showWalletAlert();
                break;
        }
    }

    private void showWalletAlert() {
        CustomAlertDialog mCustomAlertDialog = new CustomAlertDialog(context, context.getString(R.string.wallet_alert_title), context.getString(R.string.wallet_alert), 1, "", context.getResources().getString(R.string.ok), CustomAlertDialog.AlertRequestCodes.ALERT_REQUEST_WALLET_INFO, null, true, this);
        try {
            mCustomAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.JOIN_CLASS:
                User user = ((ResponseModel<User>) response).data;
                classModel.setUserJoinStatus(true);

                saved5MinClass(classModel);

                EventBroadcastHelper.sendUserUpdate(context, user);
                int joinWith;
                if (isBookWithFriendInProgress) {
                    joinWith = AppConstants.Values.UNJOIN_BOTH_CLASS; // Book With Friend
                } else {
                    joinWith = AppConstants.Values.CHANGE_AVAILABLE_SEATS_FOR_MY_CLASS;
                }
                EventBroadcastHelper.sendClassJoin(context, classModel, joinWith);
                Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);

                MixPanel.trackJoinClass(navigationFrom, classModel);
                FirebaseAnalysic.trackJoinClass(navigationFrom, classModel);
                IntercomEvents.trackJoinClass(classModel);
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
//                    context.getResources().getString(R.string.invite_friends)
                    DialogUtils.showBasicMessage(context, "",
                            context.getString(R.string.successfully_joined) + " " + classModel.getTitle()
                            , RemoteConfigConst.INVITE_FRIENDS_VALUE, new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    Helper.shareClass(context, classModel.getClassSessionId(), classModel.getTitle());

                                    finish();

                                }
                            }, context.getResources().getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();

                                    finish();
                                }
                            });

                }
                if (!CalendarHelper.haveCalendarReadWritePermissions(this)) {
                    CalendarHelper.requestCalendarReadWritePermission(this);
                } else {
                    if (CalendarHelper.haveCalendarReadWritePermissions(this)) {

                        CalendarHelper.scheduleCalenderEvent(this, classModel);
                    }
                }
                break;

            case NetworkCommunicator.RequestCode.CLASS_DETAIL:
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
                classModel = ((ResponseModel<ClassModel>) response).data;
                Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);


                if (classModel != null) {
                    getCountRating();
                    classProfileAdapter.setClass(classModel);
                    classProfileAdapter.notifyDataSetChanged();
                }
//                layoutButton.setVisibility(View.VISIBLE);
                if (Helper.isSpecialClass(classModel)) {
                    if (Helper.isFreeClass(classModel)) {
                        textViewBookWithFriend.setVisibility(View.GONE);
                    }

                }

                break;
            case NetworkCommunicator.RequestCode.CLASS_RATING_LIST:
                ratingData = ((ResponseModel<ClassRatingUserData>) response).data;
                if (classModel != null && ratingData.getCount() > 0) {
                    classModel.setNumberOfRating(ratingData.getCount());
                    classProfileAdapter.setClass(classModel);
                    classProfileAdapter.notifyDataSetChanged();
                }

                break;

            case NetworkCommunicator.RequestCode.PACKAGES_FOR_USER:
                List<Package> packagesTemp = ((ResponseModel<List<Package>>) response).data;
                if (packagesTemp != null && !packagesTemp.isEmpty()) {
                    List<Package> packages = new ArrayList<>(packagesTemp.size());
                    user = TempStorage.getUser();
                    if (mBookWithFriendData != null) {

                        for (Package aPackage : packagesTemp) {
                            int numberOfDays = DateUtils.getPackageNumberOfDays(aPackage.getDuration(), aPackage.getValidityPeriod());
                            if (!(aPackage.getDuration() > 0 && DateUtils.getDaysLeftFromPackageExpiryDate(classModel.getClassDate()) > numberOfDays)) {
                                if (aPackage.getGymVisitLimit() != 1) {
                                    packages.add(aPackage);
                                }
                            }
                        }
                        if (packages.size() == 1 || !user.isBuyMembership()) {
                            Package aPackage = packages.get(0);
                            CheckoutActivity.openActivity(context, aPackage, classModel, 2, mBookWithFriendData, aPackage.getNoOfClass());
                            return;

                        } else {
                            Package aPackage = null;
                            for (Package p : packages) {
                                if (p.getPackageType().equalsIgnoreCase(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                                    aPackage = p;

                                }
                            }
                            // MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel, mBookWithFriendData, aPackage.getNoOfClass());
                            BottomSheetClassBookingOptions mBottomSheetClassBookingOptions = BottomSheetClassBookingOptions.newInstance(classModel, mBookWithFriendData, aPackage.getNoOfClass(), aPackage);
                            mBottomSheetClassBookingOptions.show(((ClassProfileActivity) context).getSupportFragmentManager(), "friend_booking");


                            // HomeActivity.show(context,AppConstants.Tab.TAB_MY_MEMBERSHIP,AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel, mBookWithFriendData, aPackage.getNoOfClass());
                        }

                    } else {
                        for (Package aPackage : packagesTemp) {
                            int numberOfDays = DateUtils.getPackageNumberOfDays(aPackage.getDuration(), aPackage.getValidityPeriod());
                            if (!(aPackage.getDuration() > 0 && DateUtils.getDaysLeftFromPackageExpiryDate(classModel.getClassDate()) > numberOfDays)) {
                                packages.add(aPackage);
                            }
                        }
                        if (packages.size() == 1 || !user.isBuyMembership()) {
                            Package aPackage = packages.get(0);
                            CheckoutActivity.openActivity(context, aPackage, classModel, 1, aPackage.getNoOfClass());
                            return;
                        } else {
                            Package aPackage = packages.get(0);
                            textViewBook.setEnabled(true);
                            BottomSheetClassBookingOptions mBottomSheetClassBookingOptions = BottomSheetClassBookingOptions.newInstance(classModel, null, 1, aPackage);
                            mBottomSheetClassBookingOptions.show(((ClassProfileActivity) context).getSupportFragmentManager(), "own booking");

                            // HomeActivity.show(context,AppConstants.Tab.TAB_MY_MEMBERSHIP,AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
                        }
                    }
                }
                break;
            case NetworkCommunicator.RequestCode.ME_USER:
                if (Helper.isSpecialClass(classModel) &&
                        !Helper.isFreeClass(classModel)) {
                    user = TempStorage.getUser();
                    mWalletCredit = user.getWalletDto();
                    if (mWalletCredit != null && mWalletCredit.getBalance() > 0) {
                        mLayoutUserWallet.setVisibility(View.VISIBLE);
                        mTextViewWalletAmount.setText(LanguageUtils.numberConverter(mWalletCredit.getBalance()) + " " + context.getResources().getString(R.string.wallet_currency));
                    } else {
                        mLayoutUserWallet.setVisibility(View.GONE);

                    }
                }
                break;

        }

    }

    private void saved5MinClass(ClassModel classModel) {
        join5MinModel.setGetClassSessionId(classModel.getClassSessionId());
        join5MinModel.setJoiningTime(Calendar.getInstance().getTime());
        List<Join5MinModel> bookedClassList = MyPreferences.getInstance().getBookingTime();
        if (bookedClassList != null && bookedClassList.size() > 0) {
            bookedClassList.add(join5MinModel);
            MyPreferences.getInstance().saveBookingTime(bookedClassList);
            LogUtils.debug("Class Booked " + classModel.getTitle());
            return;

        } else {
            bookedClassList = new ArrayList<>();
            bookedClassList.add(join5MinModel);
            MyPreferences.getInstance().saveBookingTime(bookedClassList);

        }


    }

    @Override
    public void onApiFailure(final String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.JOIN_CLASS:
                if (errorMessage.equals("498")) {
                    if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                        final User errorResponse = MyPreferences.getInstance().getPaymentErrorResponse();

                        String message;
                        if (isBookWithFriendInProgress && mBookWithFriendData != null) {
                            if (errorResponse.getReadyPckSize() == 1) {
                                message = String.format(mContext.getString(R.string.join_fail_limit_exhaust_booking_with_friend));

                            } else {
                                message = String.format(mContext.getString(R.string.join_fail_limit_exhaust_booking_with_friend_two_ready), errorResponse.getReadyPckSize());

                            }
                        } else {
                            message = getString(R.string.join_fail_limit_exhaust);
                        }
                        DialogUtils.showBasic(context, message, getString(R.string.purchase), new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                if (isBookWithFriendInProgress && mBookWithFriendData != null) {
                                    if (errorResponse != null) {

                                        callPackageListApi(errorResponse.getReadyPckSize());
                                    } else {
                                        ToastUtils.showLong(context, errorMessage);

                                    }

                                } else {
                                    callPackageListApi(1);
                                }
                            }
                        });
                    }

                } else if (errorMessage.equals("405")) {
                    if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                        DialogUtils.showBasic(context, getString(R.string.join_share_limit_exhaust), getString(R.string.purchase), new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                if (isBookWithFriendInProgress && mBookWithFriendData != null) {
                                    User errorResponse = MyPreferences.getInstance().getPaymentErrorResponse();
                                    if (errorResponse != null) {
                                        errorMsg = 405;
                                        callPackageListApi(errorResponse.getReadyPckSize());
                                    } else {
                                        ToastUtils.showLong(context, errorMessage);

                                    }


                                } else {
                                    callPackageListApi(1);
                                }
                            }
                        });
                    }

                } else if (errorMessage.equals("402")) {
                    if (isBookWithFriendInProgress && mBookWithFriendData != null) {
                        final User errorResponse = MyPreferences.getInstance().getPaymentErrorResponse();
                        if (checkIfUserHaveExpiredPackage()) {
                            DialogUtils.showBasic(context, getString(R.string.join_fail_date_expire), getString(R.string.purchase), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    if (errorResponse != null) {
                                        callPackageListApi(errorResponse.getReadyPckSize());
                                    } else {
                                        ToastUtils.showLong(context, errorMessage);

                                    }
                                }
                            });

                        } else {
                            if (errorResponse != null) {
                                callPackageListApi(errorResponse.getReadyPckSize());
                            } else {
                                ToastUtils.showLong(context, errorMessage);

                            }
                        }


                    } else {
                        callPackageListApi(1);
                        //MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);


                    }


                } else {
                    ToastUtils.showLong(context, errorMessage);
                    textViewBook.setEnabled(true);

                }

                Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);

                break;
            case NetworkCommunicator.RequestCode.CLASS_DETAIL:
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);

                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    DialogUtils.showBasicMessage(context, errorMessage, getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    }, false);
                }

                break;
            case NetworkCommunicator.RequestCode.PACKAGES_FOR_USER:
                break;

        }
    }

    private void callPackageListApi(Integer readyPckSize) {
        if (mBookWithFriendData != null) {
            networkCommunicator.getPackagesForClass(user.getId(), classModel.getGymBranchDetail().getGymId(), classModel.getClassSessionId(), Integer.parseInt(readyPckSize.toString()), this, false);

        } else {
            networkCommunicator.getPackagesForClass(user.getId(), classModel.getGymBranchDetail().getGymId(), classModel.getClassSessionId(), Integer.parseInt(readyPckSize.toString()), this, false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        textViewBook.setEnabled(true);
        if (classModel != null)
            Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);

    }

    private boolean checkIfUserHaveExpiredPackage() {
        boolean userHaveDropinForClass = false;
        boolean userHaveExpiredDropinForClass = false;
        boolean userHaveExpiredGeneralPackageForClass = false;


        boolean userHaveGeneralPackageForClass = false;
        ArrayList<Integer> list = new ArrayList<>();
        UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());

        if (userPackageInfo.haveDropInPackage && classModel.getGymBranchDetail() != null) {
            userHaveDropinForClass = false;
            for (UserPackage userPackage : userPackageInfo.userPackageReady) {
                if ((userPackage.getGymId() == classModel.getGymBranchDetail().getGymId()) && (userPackage.getExpiryDate() == null || DateUtils.canJoinClass(classModel.getClassDate(), userPackage.getExpiryDate()) >= 0)) {
                    userHaveDropinForClass = true;
                    list.add(userPackage.getGymId());
                }
                if ((userPackage.getGymId() == classModel.getGymBranchDetail().getGymId()) && (userPackage.getExpiryDate() != null && DateUtils.canJoinClass(classModel.getClassDate(), userPackage.getExpiryDate()) < 0)) {
                    userHaveExpiredDropinForClass = true;
                }
            }

        }
        if (userPackageInfo.haveGeneralPackage) {
            if (userPackageInfo.userPackageGeneral != null) {
                if (DateUtils.canJoinClass(classModel.getClassDate(), userPackageInfo.userPackageGeneral.getExpiryDate()) >= 0) {
                    userHaveGeneralPackageForClass = true;

                }
                if (userPackageInfo.userPackageGeneral.getBalanceClass() > 0 && DateUtils.canJoinClass(classModel.getClassDate(), userPackageInfo.userPackageGeneral.getExpiryDate()) < 0) {
                    userHaveExpiredGeneralPackageForClass = true;

                }

            }
        }
        return userHaveExpiredGeneralPackageForClass;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getCountRating() {
        networkCommunicator.getClassRatingList(classModel.getClassId(), page, PAGE_LIMIT_MAIN_CLASS_LIST, this, false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == CalendarHelper.CALENDARED_PERMISSION_REQUEST_CODE) {
            if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
                if (classModel != null) {
                    if (CalendarHelper.haveCalendarReadWritePermissions(this))
                        CalendarHelper.scheduleCalenderEvent(this, classModel);
                }


            }

        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onOkClick(int requestCode, Object data) {

    }

    @Override
    public void onCancelClick(int requestCode, Object data) {

    }
}
