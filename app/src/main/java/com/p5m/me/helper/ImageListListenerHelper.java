package com.p5m.me.helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.adapters.AdapterCallbacks;

/**
 * Created by Varun John on 4/19/2018.
 */

public class ImageListListenerHelper implements AdapterCallbacks {

    public Context context;
    public Activity activity;

    public ImageListListenerHelper(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            default:
                break;
        }

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}
