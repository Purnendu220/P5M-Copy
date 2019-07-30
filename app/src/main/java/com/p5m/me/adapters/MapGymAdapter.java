package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.MapGymViewHolder;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.view.fragment.MapViewFragment;

import java.util.ArrayList;
import java.util.List;


public class MapGymAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final AdapterCallbacks<Object> adapterCallbacks;

    private List<Object> list;
    private Context context;

    private int shownInScreen;


    public MapGymAdapter(Context context, int shownInScreen, boolean showLoader, AdapterCallbacks<Object> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        this.shownInScreen = shownInScreen;

    }

    public List<Object> getList() {
        return list;
    }

    public void addClass(ClassModel model) {
        list.add(model);
    }

    public void addAllClass(List<ClassModel> models) {
        list.addAll(models);
    }

    public void clearAll() {
        list.clear();
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_map_gyms_list, parent, false);
            return new MapGymViewHolder(view, shownInScreen);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MapGymViewHolder) {
            ((MapGymViewHolder) holder).bind(getItem(position),  position);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public Object getItem(int position) {
        if (list != null && list.size() > 0)
            return list.get(position);
        else
            return null;
    }
}
