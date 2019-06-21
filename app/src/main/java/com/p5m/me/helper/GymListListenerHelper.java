package com.p5m.me.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.view.activity.Main.GymProfileActivity;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.GymDetailModel;

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
                    GymProfileActivity.open(context, gymDetailModel.getId(), shownIn);
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
