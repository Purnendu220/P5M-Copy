package com.p5m.me.view.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.GymListAdapter;
import com.p5m.me.data.main.GymDetailModel;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GymList extends BaseFragment implements ViewPagerFragmentSelection, SwipeRefreshLayout.OnRefreshListener, AdapterCallbacks, NetworkCommunicator.RequestListener {

    public static Fragment createFragment(String queryString, int position, int shownIn) {
        Fragment tabFragment = new GymList();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        bundle.putString(AppConstants.DataKey.QUERY_STRING, queryString);
        bundle.putInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, shownIn);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    @BindView(R.id.recyclerViewClass)
    public RecyclerView recyclerViewTrainers;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layoutNoData)
    public View layoutNoData;
    @BindView(R.id.imageViewEmptyLayoutImage)
    public ImageView imageViewEmptyLayoutImage;
    @BindView(R.id.textViewEmptyLayoutText)
    public TextView textViewEmptyLayoutText;

    private GymListAdapter gymListAdapter;

    private int fragmentPositionInViewPager;
    private boolean isShownFirstTime = true;
    private int page;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_MAIN_TRAINER_LIST;
    private int shownInScreen;
    private String searchedKeywords;

    public GymList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gym_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        fragmentPositionInViewPager = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);
        searchedKeywords = getArguments().getString(AppConstants.DataKey.QUERY_STRING, "");
        shownInScreen = getArguments().getInt(AppConstants.DataKey.TAB_SHOWN_IN_INT);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerViewTrainers.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewTrainers.setHasFixedSize(false);

        try {
            ((SimpleItemAnimator) recyclerViewTrainers.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        gymListAdapter = new GymListAdapter(context, activity, shownInScreen, true, this);
        recyclerViewTrainers.setAdapter(gymListAdapter);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {
        page++;
        callApiGym();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        gymListAdapter.loaderReset();
        callApiGym();
    }

    private void callApiGym() {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
            networkCommunicator.getSearchGymList(searchedKeywords , page, pageSizeLimit, this, false);
        }
    }


    @Override
    public void onTabSelection(int position) {
        if (fragmentPositionInViewPager == position && isShownFirstTime) {
            isShownFirstTime = false;

            onRefresh();
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GYM_LIST:
                swipeRefreshLayout.setRefreshing(false);
                List<GymDetailModel> models = ((ResponseModel<List<GymDetailModel>>) response).data;

                if (page == 0) {
                    gymListAdapter.clearAll();
                }

                if (!models.isEmpty()) {
                    gymListAdapter.addAll(models);

                    if (models.size() < pageSizeLimit) {
                        gymListAdapter.loaderDone();
                    }
                    gymListAdapter.notifyDataSetChanged();
                } else {
                    gymListAdapter.loaderDone();
                }

                checkListData();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GYM_LIST:

                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLong(context, errorMessage);

                checkListData();

                break;
        }
    }

    private void checkListData() {
        if (gymListAdapter.getList().isEmpty()) {
            layoutNoData.setVisibility(View.VISIBLE);
            if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
                textViewEmptyLayoutText.setText(R.string.no_data_search_gym_list);
                imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_class);
            } else {
                textViewEmptyLayoutText.setText(R.string.no_data_search_gym_list);
                imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_class);
            }
        } else {
            layoutNoData.setVisibility(View.GONE);
        }
    }

}
