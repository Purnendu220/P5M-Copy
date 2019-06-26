package com.p5m.me.adapters.viewholder;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.data.HeaderSticky;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textView)
    public TextView textView;

    private Context context;

    public HeaderViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(Object data, boolean isNoUpcomingClasses) {
        if (data != null && data instanceof HeaderSticky) {
            itemView.setVisibility(View.VISIBLE);
            HeaderSticky text = (HeaderSticky) data;

            if (!text.getTitle().isEmpty()) {
                textView.setText(text.getTitle());
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }

            if (isNoUpcomingClasses) {
                textView.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_dark_text));
                textView.setText(R.string.no_upcoming_classes);
            } else {
                textView.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
            }

        } else {
            itemView.setVisibility(View.GONE);

        }
    }
}
