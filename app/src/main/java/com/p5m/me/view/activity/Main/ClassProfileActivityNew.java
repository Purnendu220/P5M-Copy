package com.p5m.me.view.activity.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.ClassRatingUserData;
import com.p5m.me.data.UserPackageInfo;
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
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.CalendarHelper;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.BookForAFriendPopup;
import com.p5m.me.view.custom.CustomAlertDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.p5m.me.utils.AppConstants.Limit.PAGE_LIMIT_MAIN_CLASS_LIST;
import static com.p5m.me.utils.LanguageUtils.matchFitnessWord;

public class ClassProfileActivityNew extends BaseActivity implements AdapterCallbacks, View.OnClickListener, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener, CustomAlertDialog.OnAlertButtonAction {

    private String message;
    private int errorMsg;

    public static void open(Context context, ClassModel classModel, int navigationFrom) {
        context.startActivity(new Intent(context, ClassProfileActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel));
    }

    public static void openSceneTransition(Context context, ClassModel classModel, int navigationFrom, View ivProfile) {
        Intent intent = new Intent(context, ClassProfileActivityNew.class);
// Pass data object in the bundle and populate details activity.
        intent.putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        intent.putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation((Activity) context, ivProfile, "profile");
        context.startActivity(intent, options.toBundle());

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


    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.layoutButton)
    public View layoutButton;
    @BindView(R.id.textViewBook)
    public TextView textViewBook;
    @BindView(R.id.textViewBookWithFriend)
    public TextView textViewBookWithFriend;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;


    @BindView(R.id.imageViewClass)
    public ImageView imageViewClass;
    @BindView(R.id.imageViewTrainerProfile)
    public ImageView imageViewTrainerProfile;
    @BindView(R.id.imageViewMap)
    public ImageView imageViewMap;

    @BindView(R.id.textViewClassName)
    public TextView textViewClassName;
    @BindView(R.id.textViewClassCategory)
    public TextView textViewClassCategory;
    @BindView(R.id.textViewClassDate)
    public TextView textViewClassDate;
    @BindView(R.id.textViewAvailable)
    public TextView textViewAvailable;
    @BindView(R.id.textViewInfo)
    public TextView textViewInfo;
    @BindView(R.id.textViewTime)
    public TextView textViewTime;
    @BindView(R.id.textViewLocation)
    public TextView textViewLocation;
    @BindView(R.id.textViewGender)
    public TextView textViewGender;
    @BindView(R.id.textViewTrainerName)
    public TextView textViewTrainerName;
    @BindView(R.id.textViewSpecialClass)
    public TextView textViewSpecialClass;

    @BindView(R.id.textViewMap)
    public TextView textViewMap;
    @BindView(R.id.textViewMoreDetails)
    public TextView textViewMoreDetails;

    @BindView(R.id.layoutMap)
    public LinearLayout layoutMap;
    @BindView(R.id.layoutDesc)
    public LinearLayout layoutDesc;
    @BindView(R.id.layoutMoreDetails)
    public LinearLayout layoutMoreDetails;
    @BindView(R.id.layoutTrainer)
    public LinearLayout layoutTrainer;

    @BindView(R.id.layoutMapClick)
    public View layoutMapClick;

    @BindView(R.id.layoutLocation)
    public View layoutLocation;

    @BindView(R.id.studioRating)
    RatingBar studioRating;

    @BindView(R.id.textViewRatingCount)
    TextView textViewRatingCount;

    @BindView(R.id.textViewReviewCountText)
    TextView textViewReviewCountText;

    @BindView(R.id.linearLayoutStudioRating)
    LinearLayout linearLayoutStudioRating;

    @BindView(R.id.layoutSeeAllReview)
    LinearLayout layoutSeeAllReview;

    @BindView(R.id.linearLayoutClassRating)
    public LinearLayout linearLayoutClassRating;

    @BindView(R.id.textViewClassRating)
    public TextView textViewClassRating;

    @BindView(R.id.relativeLayoutFitnessLevel)
    public RelativeLayout relativeLayoutFitnessLevel;

    @BindView(R.id.imageViewClassFitnessLevel)
    public ImageView imageViewClassFitnessLevel;

    @BindView(R.id.textViewFitnessLevel)
    public TextView textViewFitnessLevel;

    private User user;
    public View imageViewOptions;
    private ClassModel classModel;
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void classPurchased(Events.ClassPurchased data) {
        handleClassJoined(data.data);
    }

    private void handleClassJoined(ClassModel data) {
        try {
            classModel.setUserJoinStatus(data.isUserJoinStatus());
            setData(classModel);
            Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_profile_new);
        ButterKnife.bind(activity);
        GlobalBus.getBus().register(this);

        classModel = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_OBJECT);
        classSessionId = getIntent().getIntExtra(AppConstants.DataKey.CLASS_SESSION_ID_INT, -1);
        navigationFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);
        FirebaseDynamicLinnk.getDynamicLink(this, getIntent());

        if (classModel == null && classSessionId == -1) {
            supportFinishAfterTransition();
            return;
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);


        message = activity.getString(R.string.non_refundable_warning_msg);

        if (classModel != null) {
            classSessionId = classModel.getClassSessionId();
            setData(classModel);

            Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);
        } else {
            layoutButton.setVisibility(View.VISIBLE);
        }
        user = TempStorage.getUser();
        onRefresh();


        setToolBar();

//        if (classModel != null) {
//            if (classModel.isUserJoinStatus()) {
//                imageViewOptions.setVisibility(View.GONE);
//            }
//        }

        MixPanel.trackClassDetails();
        onTrackingNotification();
        networkCommunicator.getMyUser(this, false);
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

        textViewBook.setText(context.getResources().getString(R.string.please_wait));
        textViewBook.setEnabled(false);

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
                ClassListListenerHelper.popupOptionsAdd(context, networkCommunicator, view, classModel, navigationFrom, null);
                break;

            case R.id.layoutUserWallet:
                showWalletAlert();
                break;
            case R.id.imageViewClass:
                if (classModel.getClassMedia() != null) {
                    Helper.openImageViewer(context, activity, view, classModel.getClassMedia().getMediaUrl(), AppConstants.ImageViewHolderType.CLASS_IMAGE_HOLDER);
                }
                break;
            case R.id.layoutTrainer:
                if (classModel.getTrainerDetail() != null) {
                    TrainerProfileActivity.open(context, classModel.getTrainerDetail(), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);
                } else {
                    GymProfileActivity.open(context, classModel.getGymBranchDetail().getGymId(), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);
                }

                break;
            case R.id.textViewLocation:
            case R.id.layoutLocation:
                GymProfileActivity.open(context, classModel.getGymBranchDetail().getGymId(), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);

                break;

            case R.id.imageViewMap:
            case R.id.layoutMapClick:


                String label = "";
                try {
                    label = classModel.getGymBranchDetail().getBranchName();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Helper.openMap(context, classModel.getGymBranchDetail().getLatitude(), classModel.getGymBranchDetail().getLongitude(), label);

                break;
            case R.id.layoutSeeAllReview:
                ViewClassRating.open(context, classModel, AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);

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

                EventBroadcastHelper.sendUserUpdate(context, user);

                classModel.setUserJoinStatus(true);
                if (isBookWithFriendInProgress)
                    EventBroadcastHelper.sendClassJoin(context, classModel, AppConstants.Values.CHANGE_AVAILABLE_SEATS_FOR_MY_CLASS);
                else
                    EventBroadcastHelper.sendClassJoin(context, classModel, AppConstants.Values.CHANGE_AVAILABLE_SEATS_FOR_MY_CLASS);
                Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);

                MixPanel.trackJoinClass(navigationFrom, classModel);
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
//                    context.getResources().getString(R.string.invite_friends)
                    DialogUtils.showBasicMessage(context, "",
                            context.getString(R.string.successfully_joined) + " " + classModel.getTitle()
                            , RemoteConfigConst.INVITE_FRIENDS_VALUE, new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    Helper.shareClass(context, classModel.getClassSessionId(), classModel.getTitle());

                                    supportFinishAfterTransition();
                                }
                            }, context.getResources().getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();

                                    supportFinishAfterTransition();
                                }
                            });

                }
                if (!CalendarHelper.haveCalendarReadWritePermissions(this)) {
                    CalendarHelper.requestCalendarReadWritePermission(this);
                } else {
                    if (CalendarHelper.haveCalendarReadWritePermissions(this))
                        CalendarHelper.scheduleCalenderEvent(this, classModel);

                }
                break;

            case NetworkCommunicator.RequestCode.CLASS_DETAIL:
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
                classModel = ((ResponseModel<ClassModel>) response).data;
                Helper.setJoinStatusProfile(context, textViewBook, textViewBookWithFriend, classModel);

                if (classModel.getAvailableSeat() < 2) {

                } else {

                }
                if (classModel != null) {
                    getCountRating();
                    setData(classModel);

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
                    setData(classModel);

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

                            Package aPackage = packages.get(0);
                            //MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel, mBookWithFriendData, aPackage.getNoOfClass());
                            HomeActivity.show(context,AppConstants.Tab.TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel, mBookWithFriendData, aPackage.getNoOfClass());
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
                            //////////
                            CheckoutActivity.openActivity(context, aPackage, classModel, 1, aPackage.getNoOfClass());
                            return;
                        } else {
                           // MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
                              HomeActivity.show(context,AppConstants.Tab.TAB_MY_MEMBERSHIP,AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);

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
                            supportFinishAfterTransition();
                        }
                    });
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
        supportFinishAfterTransition();
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

    private void setData(ClassModel model) {

        if (Helper.isSpecialClass(model)) {
            textViewSpecialClass.setVisibility(View.VISIBLE);
            textViewSpecialClass.setText(Helper.getSpecialClassText(model));

        } else {
            textViewSpecialClass.setVisibility(View.GONE);
        }

        if (!model.getDescription().isEmpty()) {

            layoutDesc.setVisibility(View.VISIBLE);
            textViewInfo.setText(model.getDescription());
        } else {
            layoutDesc.setVisibility(View.GONE);
        }

        if (model.getGymBranchDetail() != null) {

            layoutMap.setVisibility(View.VISIBLE);
            ImageUtils.setImage(context, ImageUtils.generateMapImageUrlClassDetail(model.getGymBranchDetail().getLatitude(), model.getGymBranchDetail().getLongitude()),
                    R.drawable.no_map, imageViewMap);
            textViewMap.setText(Html.fromHtml(context.getString(R.string.address) + context.getString(R.string.gaping) + model.getGymBranchDetail().getAddress()));
        } else {
            layoutMap.setVisibility(View.GONE);
        }

        String studioInstruction = "";

        if (model.getGymBranchDetail() != null) {
            studioInstruction = model.getGymBranchDetail().getStudioInstruction();
        }

        if (model.getSpecialNote().isEmpty() && model.getReminder().isEmpty() && studioInstruction.isEmpty()) {
            layoutMoreDetails.setVisibility(View.GONE);
        } else {
            layoutMoreDetails.setVisibility(View.VISIBLE);

            StringBuffer stringBuffer = new StringBuffer();

            if (!model.getReminder().isEmpty()) {
                stringBuffer.append(context.getString(R.string.remenders) + context.getString(R.string.gaping) + model.getReminder());
            }

            if (model.getGymBranchDetail() != null) {
                if (!model.getGymBranchDetail().getStudioInstruction().isEmpty()) {
                    if (!stringBuffer.toString().isEmpty()) {
                        stringBuffer.append("<br/><br/>");
                    }
                    stringBuffer.append(context.getString(R.string.studio_instruction) + context.getString(R.string.gaping) + model.getGymBranchDetail().getStudioInstruction());
                }
            }

            if (!model.getSpecialNote().isEmpty()) {
                if (!stringBuffer.toString().isEmpty()) {
                    stringBuffer.append("<br/><br/>");
                }
                stringBuffer.append(context.getString(R.string.special_note_by_trainer) + context.getString(R.string.gaping) + model.getSpecialNote());
            }

            textViewMoreDetails.setText(Html.fromHtml(stringBuffer.toString()));

        }

        if (model.getClassMedia() != null) {
            ImageUtils.setImage(context,
                    model.getClassMedia().getMediaUrl(),
                    R.drawable.class_holder, imageViewClass);
        } else {
            ImageUtils.clearImage(context, imageViewClass);
        }

        if (model.getTrainerDetail() != null) {

            textViewTrainerName.setText(model.getTrainerDetail().getFirstName());
            ImageUtils.setImage(context,
                    model.getTrainerDetail().getProfileImageThumbnail(),
                    R.drawable.profile_holder, imageViewTrainerProfile);

        } else if (model.getGymBranchDetail() != null) {

            textViewTrainerName.setText(model.getGymBranchDetail().getGymName());
            ImageUtils.setImage(context,
                    model.getGymBranchDetail().getMediaThumbNailUrl(),
                    R.drawable.profile_holder, imageViewTrainerProfile);

        } else {
            ImageUtils.clearImage(context, imageViewTrainerProfile);
            textViewLocation.setText("");
        }

        if (model.getGymBranchDetail() != null) {
            textViewLocation.setText(model.getGymBranchDetail().getGymName() + ", " + model.getGymBranchDetail().getBranchName());
        } else {
            textViewLocation.setText("");
        }
        if (model.getRating() > 0 && model.getNumberOfRating() > 0) {

            CharSequence text = String.format(context.getString(R.string.review_based_on), LanguageUtils.numberConverter(model.getNumberOfRating()) + "");
            linearLayoutStudioRating.setVisibility(View.VISIBLE);
            studioRating.setRating(model.getRating());

            textViewRatingCount.setText(LanguageUtils.numberConverter(model.getRating()) + "/" + LanguageUtils.numberConverter(5.0));
            textViewReviewCountText.setText(text);
            studioRating.setIsIndicator(true);
            linearLayoutClassRating.setVisibility(View.GONE);
            textViewClassRating.setText(model.getRating() + "");
        } else {
            linearLayoutClassRating.setVisibility(View.GONE);
            linearLayoutStudioRating.setVisibility(View.GONE);
        }

        if (model.getFitnessLevel() != null && !model.getFitnessLevel().isEmpty()) {
            relativeLayoutFitnessLevel.setVisibility(View.VISIBLE);
            switch (model.getFitnessLevel()) {
                case AppConstants.FitnessLevel.CLASS_LEVEL_BASIC:
                    imageViewClassFitnessLevel.setImageResource(R.drawable.class_level_get);
                    break;
                case AppConstants.FitnessLevel.CLASS_LEVEL_INTERMEDIATE:
                    imageViewClassFitnessLevel.setImageResource(R.drawable.class_level_set);

                    break;
                case AppConstants.FitnessLevel.CLASS_LEVEL_ADVANCED:
                    imageViewClassFitnessLevel.setImageResource(R.drawable.class_level_pro);

                    break;
                default:
                    relativeLayoutFitnessLevel.setVisibility(View.GONE);
                    break;
            }
            setTextFitnessLevel(model);
        } else {
            relativeLayoutFitnessLevel.setVisibility(View.GONE);
        }
        textViewClassName.setText(model.getTitle());
        textViewClassCategory.setText(model.getClassCategory());
        textViewClassDate.setText(DateUtils.getClassDate(model.getClassDate()));
//            textViewAvailable.setText( NumberFormat.getNumberInstance(Constants.LANGUAGE).format(model.getAvailableSeat()) + " " + context.getString(R.string.available_seats) + " ");
//                    +
//                    AppConstants.plural(context.getString(R.string.seat), model.getAvailableSeat()));
        LanguageUtils.setText(textViewAvailable, model.getAvailableSeat(), context.getString(R.string.available_seats) + " ");

        textViewTime.setText(DateUtils.getClassTime(model.getFromTime(), model.getToTime()));
        textViewGender.setText(Helper.getClassGenderText(model.getClassType()));


        layoutSeeAllReview.setOnClickListener(this);
        layoutLocation.setOnClickListener(this);


        imageViewClass.setOnClickListener(this);

        imageViewMap.setOnClickListener(this);

        layoutMapClick.setOnClickListener(this);

        textViewLocation.setOnClickListener(this);

        layoutTrainer.setOnClickListener(this);

    }

    private void setTextFitnessLevel(ClassModel model) {
        String fitnessLevel = matchFitnessWord(model.getFitnessLevel(), context);
        textViewFitnessLevel.setText(fitnessLevel);
    }
}



