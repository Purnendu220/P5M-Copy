package com.p5m.me.adapters.viewholder;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.BranchMapListAdapter;
import com.p5m.me.data.main.GymBranchDetail;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class GymBranchMapListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewName)
    public TextView textViewName;
    @BindView(R.id.textViewAddress)
    public TextView textViewAddress;

    private final Context context;

    public GymBranchMapListViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(BranchMapListAdapter branchMapListAdapter, final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof GymBranchDetail) {
            itemView.setVisibility(View.VISIBLE);

            GymBranchDetail gymBranchDetail = (GymBranchDetail) data;

            textViewName.setText(gymBranchDetail.getBranchName());
            textViewAddress.setText(gymBranchDetail.getAddress());

            if (branchMapListAdapter.getSelectedGymBranchDetail().equals(gymBranchDetail)) {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.highlight));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(GymBranchMapListViewHolder.this, itemView, data, position);
                }
            });

        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
