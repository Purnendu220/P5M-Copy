package www.gymhop.p5m.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.TrainerListAdapter;
import www.gymhop.p5m.data.gym_class.TrainerModel;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

public class TrainerList extends BaseFragment implements ViewPagerFragmentSelection, AdapterCallbacks<TrainerModel>, MyRecyclerView.LoaderCallbacks, SwipeRefreshLayout.OnRefreshListener, NetworkCommunicator.RequestListener {

    @BindView(R.id.recyclerViewClass)
    public RecyclerView recyclerViewTrainers;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private TrainerListAdapter trainerListAdapter;

    private int fragmentPositionInViewPager;
    private boolean isShownFirstTime = true;
    private int page;
    private int activityId;

    public TrainerList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainer_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        fragmentPositionInViewPager = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);
        activityId = getArguments().getInt(AppConstants.DataKey.TAB_ACTIVITY_ID);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerViewTrainers.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewTrainers.setHasFixedSize(false);

        trainerListAdapter = new TrainerListAdapter(context, true, this);
        recyclerViewTrainers.setAdapter(trainerListAdapter);
    }

    @Override
    public void onAdapterItemClick(View viewRoot, View view, TrainerModel model, int position) {
    }

    @Override
    public void onAdapterItemLongClick(View viewRoot, View view, TrainerModel model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }

    @Override
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {

    }

    @Override
    public void onTabSelection(int position) {
        if (fragmentPositionInViewPager == position && isShownFirstTime) {
            isShownFirstTime = false;

            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        networkCommunicator.getTrainerList(activityId, page,  AppConstants.Limit.PAGE_LIMIT_MAIN_TRAINER_LIST, this, false);
    }


    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.TRAINER_LIST:
                swipeRefreshLayout.setRefreshing(false);
                List<TrainerModel> classModels = ((ResponseModel<List<TrainerModel>>) response).data;

                if (!classModels.isEmpty()) {
                    trainerListAdapter.addAll(classModels);
                    trainerListAdapter.notifyDataSetChanged();
                } else {
                    checkListData();
                }
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.TRAINER_LIST:

                swipeRefreshLayout.setRefreshing(false);
                checkListData();

                break;
        }
    }

    private void checkListData() {
    }

}
