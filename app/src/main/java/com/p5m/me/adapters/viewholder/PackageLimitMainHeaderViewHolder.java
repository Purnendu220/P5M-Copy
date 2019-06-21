package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.adapters.AdapterCallbacks;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class PackageLimitMainHeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public PackageLimitMainHeaderViewHolder(View itemView, TextView textView) {
        super(itemView);

        this.textView = textView;
    }

    public void bind(Object data, AdapterCallbacks adapterCallbacks, int position) {
        if (data != null && data instanceof String) {
            itemView.setVisibility(View.VISIBLE);

            String model = (String) data;
            textView.setText(model);
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
