package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.UserPackageDetail;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by MyU10 on 3/10/2018.
 */

public class UserPackageDetailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textGymNames)
    public TextView textGymNames;
    @BindView(R.id.textGymCredits)
    public TextView textGymVisits;

    @BindView(R.id.textVisitDate)
    public TextView textVisitDate;

    private final Context context;

    public UserPackageDetailViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof UserPackageDetail) {
            itemView.setVisibility(View.VISIBLE);
            UserPackageDetail model = (UserPackageDetail) data;
            textGymNames.setText(model.getGymName() +" on " +DateUtils.getClassVisitDate(model.getClassJoinDate()));
            textVisitDate.setText(DateUtils.getClassVisitDate(model.getClassJoinDate()));
            textGymVisits.setText(String.format(context.getResources().getString(R.string.visit_times), model.getCredit()+""));
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}