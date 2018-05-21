package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.BranchMapListAdapter;
import www.gymhop.p5m.data.main.GymBranchDetail;

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
