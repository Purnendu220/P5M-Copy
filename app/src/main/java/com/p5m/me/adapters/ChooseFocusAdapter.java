package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.ChooseFocusViewHolder;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.LoaderViewHolder;
import com.p5m.me.data.ListLoader;
import com.p5m.me.data.main.ClassActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MyU10 on 3/10/2018.
 */

public class ChooseFocusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LOADER = 2;
    private static final int VIEW_TYPE_UNKNOWN = 3;

    private final AdapterCallbacks adapterCallbacks;

    private List<ClassActivity> list;
    private List<ClassActivity> selected;
    private Context context;

    private boolean showLoader;
    private ListLoader listLoader;

    public ChooseFocusAdapter(Context context, AdapterCallbacks adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        selected = new ArrayList<>();
        this.showLoader = false;
        listLoader = new ListLoader();
    }

    public void add(ClassActivity model) {
        list.add(model);
        addLoader();
    }

    public void addAll(List<ClassActivity> models) {
        list.addAll(models);
        addLoader();
    }

    public void addSelected(ClassActivity model) {
        selected.add(model);
    }

    public void removeSelected(ClassActivity model) {
        selected.remove(model);
    }

    public void addAllSelected(List<ClassActivity> models) {
        selected.addAll(models);
    }

    public List<ClassActivity> getSelected() {
        return selected;
    }

    public List<ClassActivity> getList() {
        return list;
    }

    private void addLoader() {
//        if (showLoader && list.contains(listLoader)) {
//            list.remove(listLoader);
//            list.add(listLoader);
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_choose_focus, parent, false);
            return new ChooseFocusViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ChooseFocusViewHolder) {
            ((ChooseFocusViewHolder) holder).bind(selected, getItem(position), adapterCallbacks, position);
        } else if (holder instanceof LoaderViewHolder) {
            ((LoaderViewHolder) holder).bind(listLoader, adapterCallbacks);
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