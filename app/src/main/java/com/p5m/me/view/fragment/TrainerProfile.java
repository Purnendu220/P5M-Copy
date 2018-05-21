package com.p5m.me.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.TrainerProfileAdapter;
import com.p5m.me.data.main.TrainerModel;
import com.p5m.me.helper.ClassListListenerHelper;
import com.p5m.me.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrainerProfile extends BaseFragment implements AdapterCallbacks {

    @BindView(R.id.recyclerViewProfile)
    public RecyclerView recyclerViewTrainerProfile;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private TrainerProfileAdapter trainerProfileAdapter;
    private TrainerModel trainerModel;

    public TrainerProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainer_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        trainerProfileAdapter = new TrainerProfileAdapter(context, AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE, true, this,
                new ClassListListenerHelper(context, activity, AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE, this));
        recyclerViewTrainerProfile.setAdapter(trainerProfileAdapter);

        StickyLayoutManager layoutManager = new StickyLayoutManager(context, trainerProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));

        recyclerViewTrainerProfile.setLayoutManager(layoutManager);
        layoutManager.setStickyHeaderListener(new StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                Log.d("Listener", "Attached with position: " + adapterPosition);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                Log.d("Listener", "Detached with position: " + adapterPosition);
            }
        });

//        List<Object> data = new ArrayList<>();
//        for (int count = 0; count < 7; count++) {
//            if (count == 1)
//                data.add(new HeaderSticky(""));
//            else
//                data.add(new TrainerDetail());
//        }
//
//        trainerProfileAdapter.setData(data);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}
