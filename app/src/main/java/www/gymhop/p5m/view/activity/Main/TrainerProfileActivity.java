package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.TrainerProfileAdapter;
import www.gymhop.p5m.adapters.viewholder.TrainerListViewHolder;
import www.gymhop.p5m.adapters.viewholder.TrainerProfileViewHolder;
import www.gymhop.p5m.data.FollowResponse;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.TrainerDetailModel;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.eventbus.Events;
import www.gymhop.p5m.eventbus.GlobalBus;
import www.gymhop.p5m.helper.ClassListListenerHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DialogUtils;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class TrainerProfileActivity extends BaseActivity implements AdapterCallbacks, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener, NetworkCommunicator.RequestListenerRequestDataModel<TrainerModel> {


    public static void open(Context context, TrainerModel trainerModel) {
        context.startActivity(new Intent(context, TrainerProfileActivity.class)
                .putExtra(AppConstants.DataKey.TRAINER_OBJECT, trainerModel));
    }

    public static void open(Context context, int trainerId) {
        context.startActivity(new Intent(context, TrainerProfileActivity.class)
                .putExtra(AppConstants.DataKey.TRAINER_ID_INT, trainerId));
    }

    public static Intent createIntent(Context context, int trainerId) {
        return new Intent(context, TrainerProfileActivity.class)
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

        trainerModel = (TrainerModel) getIntent().getSerializableExtra(AppConstants.DataKey.TRAINER_OBJECT);
        trainerId = getIntent().getIntExtra(AppConstants.DataKey.TRAINER_ID_INT, -1);

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

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.imageViewProfile:
                if (trainerDetailModel != null && !trainerDetailModel.getProfileImage().isEmpty()) {
                    Helper.openImageViewer(context, activity, view, trainerDetailModel.getProfileImage());
                }
                break;
            case R.id.button:
                if (viewHolder instanceof TrainerProfileViewHolder) {
                    if (trainerModel.isIsfollow()) {
                        dialogUnFollow(viewHolder, trainerModel);
                    } else {
                        ((BaseActivity) activity).networkCommunicator.followUnFollow(!trainerModel.isIsfollow(), trainerModel, this, false);
                        Helper.setFavButtonTemp(context, ((TrainerProfileViewHolder) viewHolder).button, !trainerModel.isIsfollow());
                    }
                }
                break;
        }
    }

    private void dialogUnFollow(final RecyclerView.ViewHolder viewHolder, final TrainerModel trainerModel) {

        DialogUtils.showBasic(context, "Are you sure want to unfavourite \"" + trainerModel.getFirstName() + "\" ?", "unfavourite", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                networkCommunicator.followUnFollow(!trainerModel.isIsfollow(), trainerModel, TrainerProfileActivity.this, false);

                if (viewHolder instanceof TrainerListViewHolder) {
                    Helper.setFavButtonTemp(context, ((TrainerListViewHolder) viewHolder).buttonFav, !trainerModel.isIsfollow());
                }
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

        callApiClasses();
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
                }

                break;

            case NetworkCommunicator.RequestCode.TRAINER:

                swipeRefreshLayout.setRefreshing(false);
                TrainerDetailModel trainerDetailModel = ((ResponseModel<TrainerDetailModel>) response).data;

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
