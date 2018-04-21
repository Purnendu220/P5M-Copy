package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class TrainerProfileActivity extends BaseActivity implements AdapterCallbacks, NetworkCommunicator.RequestListener {

    public static void open(Context context, TrainerModel trainerModel) {
        context.startActivity(new Intent(context, TrainerProfileActivity.class)
                .putExtra(AppConstants.DataKey.TRAINER_OBJECT, trainerModel));
    }

    @BindView(R.id.recyclerViewTrainerProfile)
    public RecyclerView recyclerViewTrainerProfile;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private TrainerProfileAdapter trainerProfileAdapter;
    private TrainerModel trainerModel;
    private TrainerDetailModel trainerDetailModel;
    private int page;

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

        trainerDetailModel = new TrainerDetailModel(trainerModel);

        trainerProfileAdapter = new TrainerProfileAdapter(context, AppConstants.AppNavigation.SHOWN_IN_TRAINER, this,
                new ClassMiniListListenerHelper(context, activity));
        recyclerViewTrainerProfile.setAdapter(trainerProfileAdapter);
        trainerProfileAdapter.setTrainerModel(trainerDetailModel);

        trainerProfileAdapter.notifyDataSetChanges();

        networkCommunicator.getUpcomingClasses(TempStorage.getUser().getId(), 0, trainerModel.getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_CLASS_LIST, this, false);
        networkCommunicator.getTrainer(trainerModel.getId(), this, false);

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

    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UPCOMING_CLASSES:
                swipeRefreshLayout.setRefreshing(false);
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (!classModels.isEmpty()) {
                    trainerProfileAdapter.addAllClass(classModels);
                    trainerProfileAdapter.notifyDataSetChanges();
                }
                break;

            case NetworkCommunicator.RequestCode.TRAINER:
                TrainerDetailModel trainerDetailModel = ((ResponseModel<TrainerDetailModel>) response).data;

                trainerProfileAdapter.setTrainerModel(trainerDetailModel);
                trainerProfileAdapter.notifyDataSetChanges();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

    }
}
