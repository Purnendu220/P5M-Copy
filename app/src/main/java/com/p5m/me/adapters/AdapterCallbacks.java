package com.p5m.me.adapters;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by MyU10 on 3/15/2018.
 */

public interface AdapterCallbacks<Object> {
    void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position);

    void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position);

    void onShowLastItem();
}
