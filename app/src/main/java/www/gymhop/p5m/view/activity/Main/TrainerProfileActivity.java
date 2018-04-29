package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.TrainerProfileAdapter;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.TrainerDetailModel;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.helper.ClassMiniListListenerHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class TrainerProfileActivity extends BaseActivity implements AdapterCallbacks, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener {


    public static void open(Context context, TrainerModel trainerModel) {
        context.startActivity(new Intent(context, TrainerProfileActivity.class)
                .putExtra(AppConstants.DataKey.TRAINER_OBJECT, trainerModel));
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerViewTrainerProfile)
    public RecyclerView recyclerViewTrainerProfile;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private TrainerProfileAdapter trainerProfileAdapter;
    private TrainerModel trainerModel;
    private TrainerDetailModel trainerDetailModel;

    private int page;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_INNER_CLASS_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile);

        ButterKnife.bind(activity);

        trainerModel = (TrainerModel) getIntent().getSerializableExtra(AppConstants.DataKey.TRAINER_OBJECT);

        if (trainerModel == null) {
            finish();
            return;
        }

        swipeRefreshLayout.setOnRefreshListener(this);

        trainerDetailModel = new TrainerDetailModel(trainerModel);

        trainerProfileAdapter = new TrainerProfileAdapter(context, AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE, true, this,
                new ClassMiniListListenerHelper(context, activity, this));
        recyclerViewTrainerProfile.setAdapter(trainerProfileAdapter);
        trainerProfileAdapter.setTrainerModel(trainerDetailModel);

        trainerProfileAdapter.notifyDataSetChanges();

        StickyLayoutManager layoutManager = new StickyLayoutManager(context, trainerProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));

        recyclerViewTrainerProfile.setLayoutManager(layoutManager);
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

        onRefresh();
        setToolBar();
    }

    private void callApiClasses() {
        networkCommunicator.getUpcomingClasses(TempStorage.getUser().getId(), 0, trainerModel.getId(), page, pageSizeLimit, this, false);
    }

    private Call callApiTrainers() {
        return networkCommunicator.getTrainer(trainerModel.getId(), this, false);
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

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.trainer));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.imageViewProfile:
                if (trainerDetailModel != null && !trainerDetailModel.getProfileImage().isEmpty()) {
                    Helper.openImageViewer(context, trainerDetailModel.getProfileImage());
                }
                break;
        }
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
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

}
