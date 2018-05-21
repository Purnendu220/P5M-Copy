package www.gymhop.p5m.helper;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.main.GymDetailModel;
import www.gymhop.p5m.view.activity.Main.GymProfileActivity;

/**
 * Created by Varun John on 4/19/2018.
 */

public class GymListListenerHelper implements AdapterCallbacks {

    public Context context;
    public Activity activity;
    private final AdapterCallbacks adapterCallbacks;
    private int shownIn;

    public GymListListenerHelper(Context context, Activity activity, int shownIn, AdapterCallbacks adapterCallbacks) {
        this.context = context;
        this.activity = activity;
        this.shownIn = shownIn;
        this.adapterCallbacks = adapterCallbacks;
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            default:
                if (model instanceof GymDetailModel) {
                    GymDetailModel gymDetailModel = (GymDetailModel) model;
                    GymProfileActivity.open(context, gymDetailModel.getId());
                }
                break;
        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }


    @Override
    public void onShowLastItem() {
        adapterCallbacks.onShowLastItem();
    }

}
