package com.p5m.me.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.LoaderViewHolder;
import com.p5m.me.adapters.viewholder.RatingViewHolder;
import com.p5m.me.adapters.viewholder.RecommendedClassViewHolder;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.ListLoader;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


public class RecommendedClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final AdapterCallbacks<ClassRatingListData> adapterCallbacks;

    private List<Object> list;
    private Context context;

    private int shownInScreen;


    public RecommendedClassAdapter(Context context, int shownInScreen, boolean showLoader, AdapterCallbacks<ClassRatingListData> adapterCallbacks) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recommended, parent, false);
            return new RecommendedClassViewHolder(view, shownInScreen);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecommendedClassViewHolder) {
            ((RecommendedClassViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
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
