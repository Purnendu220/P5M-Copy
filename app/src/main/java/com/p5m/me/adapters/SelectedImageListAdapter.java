package com.p5m.me.adapters;

import android.content.Context;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.SelectedImageViewHolder;
import com.p5m.me.data.request.SelectedFileData;
import com.p5m.me.utils.LogUtils;
import java.util.List;



public class SelectedImageListAdapter extends RecyclerView.Adapter<SelectedImageViewHolder> {
    private static final int ITEM = 0;

    private int dp;
    private List<SelectedFileData> list;
    private Context context;
    private int shownIn;
    private AdapterCallbacks<Object> adapterCallbacks;

    public SelectedImageListAdapter(Context context, int shownIn, List<SelectedFileData> mediaResponseDtoList, AdapterCallbacks<Object> adapterCallbacks) {
        this.context = context;
        list = mediaResponseDtoList;
        this.shownIn = shownIn;
        this.adapterCallbacks=adapterCallbacks;

        Fresco.initialize(context);

        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM;
    }

    @Override
    public SelectedImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_selected_image, parent, false);
       return new SelectedImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectedImageViewHolder holder, final int position) {
        LogUtils.debug("onBindViewHolder " + position);

       holder.bind(getItem(position), position, shownIn,adapterCallbacks);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<SelectedFileData> getList() {
        return list;
    }

    public SelectedFileData getItem(int position) {
        if (list != null && list.size() > 0)
            return list.get(position);
        else
            return null;
    }

}