package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class EmptyViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    public EmptyViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind() {
        itemView.setVisibility(View.GONE);
    }
}
