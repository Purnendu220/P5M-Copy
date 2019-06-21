package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ChooseFocusAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.request.ChooseFocusRequest;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseFocusActivity extends BaseActivity implements AdapterCallbacks, NetworkCommunicator.RequestListener {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, ChooseFocusActivity.class));
    }

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.progressBarDone)
    public ProgressBar progressBarDone;
    @BindView(R.id.imageViewDone)
    public ImageView imageViewDone;

    private ChooseFocusAdapter chooseFocusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_focus);

        ButterKnife.bind(activity);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        chooseFocusAdapter = new ChooseFocusAdapter(context, this);
        recyclerView.setAdapter(chooseFocusAdapter);

        if (TempStorage.getUser().getClassCategoryList() != null) {

            List<ClassActivity> classActivities = new ArrayList<>(TempStorage.getActivities().size());

            for (ClassActivity classActivity : TempStorage.getUser().getClassCategoryList()) {
                classActivities.add(new ClassActivity(classActivity.getId(), classActivity.getClassCategoryId(), classActivity.getClassCategoryName()));
            }
            chooseFocusAdapter.addAllSelected(classActivities);
        }

        if (TempStorage.getActivities() != null) {

            List<ClassActivity> classActivities = new ArrayList<>(TempStorage.getActivities().size());

            for (ClassActivity classActivity : TempStorage.getActivities()) {
                classActivities.add(new ClassActivity(classActivity.getId(), classActivity.getName()));
            }
            chooseFocusAdapter.addAll(classActivities);
        }

        chooseFocusAdapter.notifyDataSetChanged();
        onTrackingNotification();

    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

    @OnClick(R.id.imageViewDone)
    public void imageViewDone(View view) {

        imageViewDone.setVisibility(View.GONE);
        progressBarDone.setVisibility(View.VISIBLE);

        List<ClassActivity> selectedNow = chooseFocusAdapter.getSelected();

        List<ClassActivity> selectedPrevious = new ArrayList<>();
        List<ClassActivity> deleted = new ArrayList<>();

        // if are previously selected send their Ids else 0...
        List<ClassActivity> selectedNowWithIds = new ArrayList<>();

        if (TempStorage.getUser().getClassCategoryList() != null) {
            selectedPrevious.addAll(TempStorage.getUser().getClassCategoryList());
        }

        for (ClassActivity selectedPreviously : selectedPrevious) {
            if (!selectedNow.contains(selectedPreviously)) {
                deleted.add(selectedPreviously);
            }
        }

        for (ClassActivity selected : selectedNow) {
            if (selectedPrevious.contains(selected)) {
                selectedNowWithIds.add(selectedPrevious.get(selectedPrevious.indexOf(selected)));
            } else {
                selectedNowWithIds.add(selected);
            }
        }

        callApi(deleted, selectedNowWithIds);
    }

    private void callApi(List<ClassActivity> deleted, List<ClassActivity> selectedNow) {
        List<Integer> deletedIds = new ArrayList<>(deleted.size());

        for (ClassActivity classActivity : deleted) {
            deletedIds.add(classActivity.getId());
        }
        networkCommunicator.updateUserFocus(TempStorage.getUser().getId(), new ChooseFocusRequest(selectedNow, deletedIds), this, false);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        if (model instanceof ClassActivity) {
            ClassActivity classActivity = (ClassActivity) model;

            if (!chooseFocusAdapter.getSelected().contains(classActivity)) {
                chooseFocusAdapter.addSelected(classActivity);
            } else {
                chooseFocusAdapter.removeSelected(classActivity);
            }

            try {
                chooseFocusAdapter.notifyItemChanged(chooseFocusAdapter.getList().indexOf(classActivity));
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
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
            case NetworkCommunicator.RequestCode.UPDATE_USER:
                networkCommunicator.getMyUser(this, false);
                break;

            case NetworkCommunicator.RequestCode.ME_USER:
                imageViewDone.setVisibility(View.VISIBLE);
                progressBarDone.setVisibility(View.GONE);

                if (TempStorage.getUser().getClassCategoryList() != null && !TempStorage.getUser().getClassCategoryList().isEmpty()) {
                    MixPanel.trackEditProfileFocus(TempStorage.getUser().getClassCategoryList());
                }

                finish();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        imageViewDone.setVisibility(View.VISIBLE);
        progressBarDone.setVisibility(View.GONE);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UPDATE_USER:
                ToastUtils.show(context, errorMessage);
                break;
        }
    }
}
