package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.LanguageModel;
import com.p5m.me.data.main.Transaction;
import com.p5m.me.fxn.utility.Constants;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.JsonUtils;
import com.p5m.me.utils.LanguageUtils;

import java.util.Locale;

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
    @BindView(R.id.textViewAmount)
    public TextView textViewAmount;

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

            textViewAmount.setText(String.format(context.getString(R.string.amount_transaction_history),TempStorage.getUser().getCurrencyCode()));


            Transaction model = (Transaction) data;

            String refId = String.valueOf(LanguageUtils.numberConverter(Double.parseDouble(model.getReferenceId())));

            if (Locale.ENGLISH == Constants.LANGUAGE)
                refId = refId.replace(",", "");
            else
                refId = refId.replace("٬", "");

            textViewRefId.setText(refId);
            textViewPackageName.setText(model.getPackageName());
            textViewPackagePrice.setText(LanguageUtils.numberConverter(model.getAmount(), 2) + "");
            textViewPackageDate.setText(DateUtils.getTransactionDate(model.getDate()));
            textViewPackageStatus.setText(model.getStatus().toUpperCase());
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
