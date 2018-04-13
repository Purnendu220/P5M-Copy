package www.gymhop.p5m.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import www.gymhop.p5m.adapters.ClassMiniViewAdapter;
import www.gymhop.p5m.data.gym_class.ClassModel;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

public class ClassMiniViewList extends BaseFragment implements ViewPagerFragmentSelection, MyRecyclerView.LoaderCallbacks, SwipeRefreshLayout.OnRefreshListener, AdapterCallbacks<ClassModel>, NetworkCommunicator.RequestListener {

    public static Fragment getInstance(int position, int shownIn) {
        Fragment tabFragment = new ClassMiniViewList();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        bundle.putInt(AppConstants.DataKey.TAB_SHOWN_IN, shownIn);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    @BindView(R.id.recyclerViewClass)
    public RecyclerView recyclerViewClass;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private ClassMiniViewAdapter classListAdapter;

    private int fragmentPositionInViewPager;
    private boolean isShownFirstTime = true;
    private int page;

    private int shownInScreen;

    public ClassMiniViewList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_mini_view_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        fragmentPositionInViewPager = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);
        shownInScreen = getArguments().getInt(AppConstants.DataKey.TAB_SHOWN_IN);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerViewClass.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewClass.setHasFixedSize(false);

        classListAdapter = new ClassMiniViewAdapter(context, shownInScreen, true, this);
        recyclerViewClass.setAdapter(classListAdapter);
    }

    @Override
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {
        LogUtils.debug("onShowLoader " + position);
    }

    @Override
    public void onAdapterItemClick(View viewRoot, View view, ClassModel model, int position) {
    }

    @Override
    public void onAdapterItemLongClick(View viewRoot, View view, ClassModel model, int position) {
    }

    @Override
    public void onShowLastItem() {
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

        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE) {
            networkCommunicator.getScheduleList(TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_CLASS_LIST, this, false);

        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_WISH_LIST) {
            networkCommunicator.getWishList(TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_CLASS_LIST, this, false);
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.TRAINER_LIST:
                swipeRefreshLayout.setRefreshing(false);
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (!classModels.isEmpty()) {
                    classListAdapter.addAllClass(classModels);
                    classListAdapter.notifyDataSetChanged();
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
