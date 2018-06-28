package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.Transaction;
import com.p5m.me.utils.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewRefId)
    public TextView textViewRefId;
    @BindView(R.id.textViewPackageName)
    public TextView textViewPackageName;
    @BindView(R.id.textViewPackagePrice)
    public TextView textViewPackagePrice;
    @BindView(R.id.textViewPackageDate)
    public TextView textViewPackageDate;
    @BindView(R.id.textViewPackageStatus)
    public TextView textViewPackageStatus;

    private final Context context;

    public TransactionViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof Transaction) {
            itemView.setVisibility(View.VISIBLE);

            Transaction model = (Transaction) data;

            textViewRefId.setText(model.getReferenceId());
            textViewPackageName.setText(model.getPackageName());
            textViewPackagePrice.setText(model.getAmount() + "");
            textViewPackageDate.setText(DateUtils.getTransactionDate(model.getDate()));
            textViewPackageStatus.setText(model.getStatus().toUpperCase());

        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}