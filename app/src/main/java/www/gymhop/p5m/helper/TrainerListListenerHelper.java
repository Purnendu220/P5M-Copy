package www.gymhop.p5m.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.viewholder.TrainerListViewHolder;
import www.gymhop.p5m.data.FollowResponse;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.utils.DialogUtils;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.Main.TrainerProfileActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;

/**
 * Created by Varun John on 4/19/2018.
 */

public class TrainerListListenerHelper implements AdapterCallbacks, NetworkCommunicator.RequestListener, NetworkCommunicator.RequestListenerRequestDataModel<TrainerModel> {

    private final AdapterCallbacks adapterCallbacks;
    public Context context;
    public Activity activity;

    public TrainerListListenerHelper(Context context, Activity activity, AdapterCallbacks adapterCallbacks) {
        this.context = context;
        this.activity = activity;
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
                        }
                    }
                }
                break;
            default:
                TrainerProfileActivity.open(context, (TrainerModel) model);
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
