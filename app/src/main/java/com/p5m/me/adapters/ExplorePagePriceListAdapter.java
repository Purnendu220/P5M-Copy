package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.PriceModelViewHolder;
import com.p5m.me.data.PriceModel;

import java.util.List;


public class ExplorePagePriceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final AdapterCallbacks<Object> adapterCallbacks;

    private List<PriceModel> list;
    private Context context;

    private int shownInScreen;


    public ExplorePagePriceListAdapter(Context context, int shownInScreen, List<PriceModel> list, AdapterCallbacks<Object> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        this.list = list;

        this.shownInScreen = shownInScreen;


    }

    public List<PriceModel> getList() {
        return list;
    }

    public void addClass(PriceModel model) {
        list.add(model);
    }

    public void addAllClass(List<PriceModel> models) {
        list.addAll(models);
        notifyDataSetChanged();
    }

    public void clearAll() {
        list.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_price, parent, false);
        return new PriceModelViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PriceModelViewHolder) {
            ((PriceModelViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
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
