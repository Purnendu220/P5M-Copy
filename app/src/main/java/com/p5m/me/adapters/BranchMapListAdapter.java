package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.GymBranchMapListViewHolder;
import com.p5m.me.adapters.viewholder.LoaderViewHolder;
import com.p5m.me.data.ListLoader;
import com.p5m.me.data.main.GymBranchDetail;

import java.util.ArrayList;
import java.util.List;


public class BranchMapListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LOADER = 2;
    private static final int VIEW_TYPE_UNKNOWN = 3;

    private final AdapterCallbacks adapterCallbacks;

    private List<GymBranchDetail> list;
    private GymBranchDetail selectedGymBranchDetail;
    private Context context;

    private boolean showLoader;
    private ListLoader listLoader;

    public BranchMapListAdapter(Context context, boolean showLoader, AdapterCallbacks adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        this.showLoader = showLoader;
        listLoader = new ListLoader();
    }

    public GymBranchDetail getSelectedGymBranchDetail() {
        return selectedGymBranchDetail;
    }

    public void setSelectedGymBranchDetail(GymBranchDetail selectedGymBranchDetail) {
        this.selectedGymBranchDetail = selectedGymBranchDetail;
    }

    public void add(GymBranchDetail model) {
        list.add(model);
        addLoader();
    }

    public void addAll(List<GymBranchDetail> models) {
        list.addAll(models);
        setSelectedGymBranchDetail(models.get(0));
        addLoader();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_branch_map_list, parent, false);
            return new GymBranchMapListViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof GymBranchMapListViewHolder) {
            ((GymBranchMapListViewHolder) holder).bind(this, getItem(position), adapterCallbacks, position);
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