package com.p5m.me.adapters.viewholder;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ProfileHeaderTabViewHolder extends RecyclerView.ViewHolder {

    public static final int TAB_1 = 1;
    public static final int TAB_2 = 2;

    @BindView(R.id.header1)
    public TextView header1;
    @BindView(R.id.header2)
    public TextView header2;

    @BindView(R.id.header1Indicator)
    public View header1Indicator;
    @BindView(R.id.header2Indicator)
    public View header2Indicator;

    private final Context context;

    public ProfileHeaderTabViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(final int selectedTab, final Object model, final AdapterCallbacks<Object> adapterCallbacks, final int position) {

        switch (selectedTab) {
            case TAB_1:

                header1.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                header2.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                break;
            case TAB_2:

                header1.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                header2.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                break;
        }

        header1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != TAB_1) {
                    adapterCallbacks.onAdapterItemClick(ProfileHeaderTabViewHolder.this, header1, model, position);
                    header1.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                    header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                    header2.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                    header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                }
            }
        });
        header2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != TAB_2) {
                    adapterCallbacks.onAdapterItemClick(ProfileHeaderTabViewHolder.this, header2, model, position);
                    header1.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                    header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                    header2.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                    header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                }
            }
        });
    }
}
