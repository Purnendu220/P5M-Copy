package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.PaymentOptionViewHolder;
import com.p5m.me.data.main.PaymentInitiateModel;

import java.util.ArrayList;
import java.util.List;

public class PaymentOptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_UNKNOWN = 3;
    private static final int VIEW_TYPE_PAYMENT_OPTION_LIST = 1;

    private final AdapterCallbacks adapterCallbacks;

    private List<Object> list;
    private Context context;

    public PaymentOptionAdapter(Context context, boolean showLoader, AdapterCallbacks adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
    }

    public List<Object> getList() {
        return list;
    }

    public void remove(int index) {
        list.remove(index);
    }

    public void add(PaymentInitiateModel.DataBean.PaymentMethodsBean model) {
        list.add(model);
    }

    public void addAll(List<PaymentInitiateModel.DataBean.PaymentMethodsBean> models) {
        list.clear();
        list.addAll(models);
        notifyDataSetChanged();
    }

    public void clearAll() {
        list.clear();
    }


    @Override
    public int getItemViewType(int position) {
        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (item instanceof PaymentInitiateModel.DataBean.PaymentMethodsBean) {
            itemViewType = VIEW_TYPE_PAYMENT_OPTION_LIST;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PAYMENT_OPTION_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_payment_options, parent, false);
            return new PaymentOptionViewHolder(view);
        }
        return new EmptyViewHolder(new LinearLayout(context));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PaymentOptionViewHolder) {
            ((PaymentOptionViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Object getItem(int position) {
        if (list != null && list.size() > 0)
            return list.get(position);
        else
            return null;
    }
}