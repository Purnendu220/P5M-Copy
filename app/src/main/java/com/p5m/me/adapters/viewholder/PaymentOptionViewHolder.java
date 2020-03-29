package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.Nationality;
import com.p5m.me.data.main.PaymentInitiateModel;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class PaymentOptionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textView)
    public TextView textView;
    @BindView(R.id.layoutItem)
    public View layoutItem;
    @BindView(R.id.imageView)
    public ImageView imageView;

    private final Context context;

    public PaymentOptionViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof PaymentInitiateModel.DataBean.PaymentMethodsBean) {
            itemView.setVisibility(View.VISIBLE);

            PaymentInitiateModel.DataBean.PaymentMethodsBean model = (PaymentInitiateModel.DataBean.PaymentMethodsBean) data;

            if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar"))
                textView.setText(model.getPaymentMethodAr());
            else
                textView.setText(model.getPaymentMethodEn());

            ImageUtils.setImage(context,model.getImageUrl(),imageView);
            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(PaymentOptionViewHolder.this, itemView, data, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}
