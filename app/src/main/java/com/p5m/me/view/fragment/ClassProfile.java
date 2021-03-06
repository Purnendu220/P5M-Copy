package com.p5m.me.view.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;
import com.google.android.material.appbar.AppBarLayout;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.MyProfileAdapter;
import com.p5m.me.adapters.viewholder.ProfileHeaderTabViewHolder;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.TrainerModel;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.activity.custom.MyRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassProfile extends BaseFragment implements AdapterCallbacks<Object>, MyRecyclerView.LoaderCallbacks, NetworkCommunicator.RequestListener {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private MyProfileAdapter myProfileAdapter;
    private int page=0;

    public ClassProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        setToolBar();

//        myProfileAdapter = new MyProfileAdapter(context, this);
        StickyLayoutManager layoutManager = new StickyLayoutManager(context, myProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));
        layoutManager.setAutoMeasureEnabled(false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(myProfileAdapter);
        myProfileAdapter.setUser(TempStorage.getUser());
        recyclerView.setHasFixedSize(true);

        layoutManager.setStickyHeaderListener(new StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                maintainHeaderState(headerView);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                maintainHeaderState(headerView);
            }
        });

        if (myProfileAdapter.getTrainers().isEmpty()) {
            networkCommunicator.getFavTrainerList(AppConstants.ApiParamValue.FOLLOW_TYPE_FOLLOWED, TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
            myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_1);
        }
    }

    /**
     * To fix and maintain the sticky header tab selection state bug..
     */
    private void maintainHeaderState(View headerView) {
        ProfileHeaderTabViewHolder profileHeaderTabViewHolder;
        if (myProfileAdapter.getProfileHeaderTabViewHolder() != null) {
            profileHeaderTabViewHolder = myProfileAdapter.getProfileHeaderTabViewHolder();
        } else {
            profileHeaderTabViewHolder = new ProfileHeaderTabViewHolder(headerView);
        }

        switch (myProfileAdapter.getSelectedTab()) {
            case ProfileHeaderTabViewHolder.TAB_1:

                profileHeaderTabViewHolder.header1.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                profileHeaderTabViewHolder.header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                profileHeaderTabViewHolder.header2.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                profileHeaderTabViewHolder.header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                break;
            case ProfileHeaderTabViewHolder.TAB_2:

                profileHeaderTabViewHolder.header1.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                profileHeaderTabViewHolder.header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                profileHeaderTabViewHolder.header2.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                profileHeaderTabViewHolder.header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                break;
        }
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

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_my_profile, null);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.header1: {
                if (myProfileAdapter.getTrainers().isEmpty()) {
                    networkCommunicator.getFavTrainerList(AppConstants.ApiParamValue.FOLLOW_TYPE_FOLLOWED, TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
                }

                myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_1);
                myProfileAdapter.notifyDataSetChanges();
            }
            break;
            case R.id.header2: {
                if (myProfileAdapter.getClasses().isEmpty()) {
                    networkCommunicator.getFinishedClassList(TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_UNLIMITED, this, false);
                }

                myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_2);
                myProfileAdapter.notifyDataSetChanges();
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
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CLASS_LIST:
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (!classModels.isEmpty()) {
                    myProfileAdapter.addClasses(classModels);
                    myProfileAdapter.notifyDataSetChanges();
                } else {
                    checkListData();
                }

                break;
            case NetworkCommunicator.RequestCode.TRAINER_LIST:
                List<TrainerModel> trainerModels = ((ResponseModel<List<TrainerModel>>) response).data;

                if (!trainerModels.isEmpty()) {
                    myProfileAdapter.addTrainers(trainerModels);
                    myProfileAdapter.notifyDataSetChanges();
                } else {
                    checkListData();
                }

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

    }

    private void checkListData() {
    }

}
