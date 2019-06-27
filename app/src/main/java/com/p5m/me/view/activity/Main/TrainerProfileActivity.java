package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.TrainerProfileAdapter;
import com.p5m.me.adapters.viewholder.TrainerListViewHolder;
import com.p5m.me.adapters.viewholder.TrainerProfileViewHolder;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.FollowResponse;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.TrainerDetailModel;
import com.p5m.me.data.main.TrainerModel;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.firebase_dynamic_link.FirebaseDynamicLinnk;
import com.p5m.me.helper.ClassListListenerHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainerProfileActivity extends BaseActivity implements AdapterCallbacks, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener, NetworkCommunicator.RequestListenerRequestDataModel<TrainerModel> {

    public static void open(Context context, TrainerModel trainerModel, int navigationFrom) {
        context.startActivity(new Intent(context, TrainerProfileActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstants.DataKey.TRAINER_OBJECT, trainerModel));
    }

    public static void open(Context context, int trainerId, int navigationFrom) {
        context.startActivity(new Intent(context, TrainerProfileActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstants.DataKey.TRAINER_ID_INT, trainerId));
    }

    public static Intent createIntent(Context context, int trainerId, int navigationFrom) {
        return new Intent(context, TrainerProfileActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstants.DataKey.TRAINER_ID_INT, trainerId);
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerViewProfile)
    public RecyclerView recyclerViewProfile;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private TrainerProfileAdapter trainerProfileAdapter;
    private TrainerModel trainerModel;
    private TrainerDetailModel trainerDetailModel;

    private int trainerId;
    private int navigatedFrom;

    private int page;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_INNER_CLASS_LIST;

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void trainerFollowed(Events.TrainerFollowed trainerFollowed) {
        try {

            trainerProfileAdapter.getTrainerModel().setIsfollow(trainerFollowed.isFollowed);
            trainerProfileAdapter.notifyItemChanged(0);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void waitlistJoin(Events.WaitlistJoin data) {
        handleWaitlistJoined(data.data);

    }

    private void handleWaitlistJoined(ClassModel data) {
        try {
            int index = trainerProfileAdapter.getList().indexOf(data);
            if (index != -1) {
                Object obj = trainerProfileAdapter.getList().get(index);
                if (obj instanceof ClassModel) {
                    ClassModel classModel = (ClassModel) obj;
                    Helper.setWaitlistAddData(classModel, data);
//code to commit
                    trainerProfileAdapter.notifyItemChanged(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

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
    public void classPurchased(Events.ClassPurchased data) {
        handleClassJoined(data.data);
    }

    private void handleClassJoined(ClassModel data) {
        try {
            int index = trainerProfileAdapter.getList().indexOf(data);
            if (index != -1) {
                Object obj = trainerProfileAdapter.getList().get(index);
                if (obj instanceof ClassModel) {
                    ClassModel classModel = (ClassModel) obj;
                    Helper.setClassJoinEventData(classModel, data);

                    trainerProfileAdapter.notifyItemChanged(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_gym_profile);

        ButterKnife.bind(activity);
        GlobalBus.getBus().register(this);
        FirebaseDynamicLinnk.getDynamicLink(this,getIntent());
        trainerModel = (TrainerModel) getIntent().getSerializableExtra(AppConstants.DataKey.TRAINER_OBJECT);
        trainerId = getIntent().getIntExtra(AppConstants.DataKey.TRAINER_ID_INT, -1);
        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);

        if (trainerModel == null && trainerId == -1) {
            finish();
            return;
        }

        if (trainerModel != null) {
            trainerId = trainerModel.getId();
        }

        swipeRefreshLayout.setOnRefreshListener(this);

        trainerDetailModel = new TrainerDetailModel(trainerModel);

        trainerProfileAdapter = new TrainerProfileAdapter(context, AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE, true, this,
                new ClassListListenerHelper(context, activity, AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE, this));
        recyclerViewProfile.setAdapter(trainerProfileAdapter);
        trainerProfileAdapter.setTrainerModel(trainerDetailModel);

        if (trainerDetailModel != null) {
            trainerProfileAdapter.setTrainerModel(trainerDetailModel);
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }

        StickyLayoutManager layoutManager = new StickyLayoutManager(context, trainerProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));

        recyclerViewProfile.setLayoutManager(layoutManager);
        layoutManager.setStickyHeaderListener(new StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                LogUtils.debug("Listener Attached with position: " + adapterPosition);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                LogUtils.debug("Listener Detached with position: " + adapterPosition);
            }
        });

        try {
            ((SimpleItemAnimator) recyclerViewProfile.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        onRefresh();
        setToolBar();

        MixPanel.trackTrainerVisit(navigatedFrom);
    }

    private void callApiClasses() {
        networkCommunicator.getUpcomingClasses(TempStorage.getUser().getId(), 0, trainerId, page, pageSizeLimit, this, false);
    }

    private void callApiTrainers() {
        networkCommunicator.getTrainer(trainerId, this, false);
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

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_normal, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.trainer_profile));

        ImageView imageViewShare = v.findViewById(R.id.imageViewShare);

        imageViewShare.setVisibility(View.VISIBLE);
        imageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Helper.shareTrainer(context, trainerId, trainerModel.getFirstName());
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.exception(e);
                }
            }
        });

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.imageViewProfile:
                if (trainerDetailModel != null &&trainerDetailModel.getProfileImage() != null&& !trainerDetailModel.getProfileImage().isEmpty()) {
                    Helper.openImageViewer(context, activity, view, trainerDetailModel.getProfileImage(),AppConstants.ImageViewHolderType.PROFILE_IMAGE_HOLDER);
                }
                break;
            case R.id.imageViewCover:
                if (trainerDetailModel != null &&trainerDetailModel.getCoverImage() != null&& !trainerDetailModel.getCoverImage().isEmpty()) {
                    Helper.openImageViewer(context, activity, view, trainerDetailModel.getCoverImage(),AppConstants.ImageViewHolderType.COVER_IMAGE_HOLDER);
                }
                break;

            case R.id.button:
                if (viewHolder instanceof TrainerProfileViewHolder) {
                    if (trainerModel != null) {
                        if (trainerModel.isIsfollow()) {
                            dialogUnFollow(viewHolder, trainerModel);
                        } else {
                            ((BaseActivity) activity).networkCommunicator.followUnFollow(!trainerModel.isIsfollow(), trainerModel, this, false);
                            Helper.setFavButtonTemp(context, ((TrainerProfileViewHolder) viewHolder).button, !trainerModel.isIsfollow());

                            MixPanel.trackAddFav(AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE, trainerModel.getFirstName());
                        }
                    }
                }
                break;


        }
    }

    private void dialogUnFollow(final RecyclerView.ViewHolder viewHolder, final TrainerModel trainerModel) {

        DialogUtils.showBasic(context, getString(R.string.are_you_sure_you_want_to_remove)+" " + trainerModel.getFirstName() + "?", context.getString(R.string.yes), new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                networkCommunicator.followUnFollow(!trainerModel.isIsfollow(), trainerModel, TrainerProfileActivity.this, false);

                if (viewHolder instanceof TrainerListViewHolder) {
                    Helper.setFavButtonTemp(context, ((TrainerListViewHolder) viewHolder).buttonFav, !trainerModel.isIsfollow());
                }

                MixPanel.trackRemoveFav(AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE, trainerModel.getFirstName());
            }
        });
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {
        page++;
        callApiClasses();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        trainerProfileAdapter.loaderReset();

        callApiTrainers();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UPCOMING_CLASSES:
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (page == 0) {
                    trainerProfileAdapter.clearAllClasses();
                }

                if (!classModels.isEmpty()) {
                    trainerProfileAdapter.addAllClass(classModels);
                    if (classModels.size() < pageSizeLimit) {
                        trainerProfileAdapter.loaderDone();
                    }
                    trainerProfileAdapter.notifyDataSetChanges();
                } else {
                    trainerProfileAdapter.loaderDone();
                    trainerProfileAdapter.addAllClass(classModels);

                    trainerProfileAdapter.notifyDataSetChanges();
                }

                break;

            case NetworkCommunicator.RequestCode.TRAINER:

                callApiClasses();
                swipeRefreshLayout.setRefreshing(false);
                trainerDetailModel = ((ResponseModel<TrainerDetailModel>) response).data;
                trainerModel = trainerDetailModel.getTrainer();

                trainerProfileAdapter.setTrainerModel(trainerDetailModel);
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.TRAINER:

                trainerProfileAdapter.notifyDataSetChanges();

                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLong(context, errorMessage);
                break;
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode, TrainerModel requestDataModel) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.FOLLOW:
            case NetworkCommunicator.RequestCode.UN_FOLLOW:
                FollowResponse data = ((ResponseModel<FollowResponse>) response).data;

                requestDataModel.setIsfollow(data.getFollow());
                EventBroadcastHelper.trainerFollowUnFollow(requestDataModel, data.getFollow());
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode, TrainerModel requestDataModel) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.FOLLOW:
            case NetworkCommunicator.RequestCode.UN_FOLLOW:
                ToastUtils.showLong(context, errorMessage);
                break;
        }
    }
}
