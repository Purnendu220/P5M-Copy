package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.HeaderSticky;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class MemberShipHeaderViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;

    public MemberShipHeaderViewHolder(View itemView, TextView textView) {
        super(itemView);
        this.textView = textView;
    }

    public void bind(String usdInfo, Object data, AdapterCallbacks adapterCallbacks, int position) {
        if (usdInfo != null) {
            String text = (String) data;
            textView.setText(text);
            if (!text.isEmpty()) {
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
            }
           /* HeaderSticky text = (HeaderSticky) data;

            if (!text.getTitle().isEmpty()) {
                textView.setText(text.getTitle());
                itemView.setVisibility(View.GONE);
            } else {
                itemView.setVisibility(View.GONE);
            }*/
        } else {
            itemView.setVisibility(View.GONE);

        }
    }
}
