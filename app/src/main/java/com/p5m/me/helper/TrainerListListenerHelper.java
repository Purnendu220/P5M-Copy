package com.p5m.me.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.viewholder.TrainerListViewHolder;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.FollowResponse;
import com.p5m.me.data.main.TrainerModel;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.TrainerProfileActivity;
import com.p5m.me.view.activity.base.BaseActivity;

/**
 * Created by Varun John on 4/19/2018.
 */

public class TrainerListListenerHelper implements AdapterCallbacks, NetworkCommunicator.RequestListener, NetworkCommunicator.RequestListenerRequestDataModel<TrainerModel> {

    private final AdapterCallbacks adapterCallbacks;
    public Context context;
    public Activity activity;

    public int shownInScreen;

    public TrainerListListenerHelper(Context context, Activity activity, int shownInScreen, AdapterCallbacks adapterCallbacks) {
        this.context = context;
        this.activity = activity;
        this.shownInScreen = shownInScreen;

        this.adapterCallbacks = adapterCallbacks;
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.buttonFav:
                if (model instanceof TrainerModel) {
                    TrainerModel trainerModel = (TrainerModel) model;

                    if (viewHolder instanceof TrainerListViewHolder) {
                        if (trainerModel.isIsfollow()) {
                            dialogUnFollow(viewHolder, trainerModel);
                        } else {
                            ((BaseActivity) activity).networkCommunicator.followUnFollow(!trainerModel.isIsfollow(), trainerModel, this, false);
                            Helper.setFavButtonTemp(context, ((TrainerListViewHolder) viewHolder).buttonFav, !trainerModel.isIsfollow());

                            MixPanel.trackAddFav(shownInScreen, trainerModel.getFirstName());
                        }
                    }
                }
                break;
            default:
                TrainerProfileActivity.open(context, (TrainerModel) model, shownInScreen);
                break;
        }
    }

    private void dialogUnFollow(final RecyclerView.ViewHolder viewHolder, final TrainerModel trainerModel) {

        DialogUtils.showBasic(context, "Are you sure you want to remove " + trainerModel.getFirstName() + "?", "Yes", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                ((BaseActivity) activity).networkCommunicator.followUnFollow(!trainerModel.isIsfollow(), trainerModel, TrainerListListenerHelper.this, false);

                if (viewHolder instanceof TrainerListViewHolder) {
                    Helper.setFavButtonTemp(context, ((TrainerListViewHolder) viewHolder).buttonFav, !trainerModel.isIsfollow());
                }

                MixPanel.trackRemoveFav(shownInScreen, trainerModel.getFirstName());
            }
        });
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {
        adapterCallbacks.onShowLastItem();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

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
