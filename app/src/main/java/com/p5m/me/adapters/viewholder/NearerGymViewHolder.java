package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.adapters.AdapterCallbacks;

import butterknife.ButterKnife;

public class NearerGymViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private int shownInScreen;

    public NearerGymViewHolder(View view, int shownInScreen) {
        super(view);

        context = view.getContext();
        ButterKnife.bind(this, view);
        this.shownInScreen = shownInScreen;
    }

    public void bind(Object item, final int position) {

    }
}
