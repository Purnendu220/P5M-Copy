package com.p5m.me.agorartc.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.utils.AppConstants;

import java.util.ArrayList;

import io.agora.rtc.video.VideoEncoderConfiguration;

public class ResolutionAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private int mSelected;
    private ArrayList<ResolutionItem> mItems = new ArrayList<>();

    public ResolutionAdapter(Context context, int selected) {
        mContext = context;
        mSelected = selected;
        initData(context);
    }

    private void initData(Context context) {
        int size = AppConstants.VIDEO_DIMENSIONS.length;
        String[] labels = context.getResources().
                getStringArray(R.array.string_array_resolutions);

        for (int i = 0; i < size; i++) {
            ResolutionItem item = new ResolutionItem(labels[i],AppConstants.VIDEO_DIMENSIONS[i]);
            mItems.add(item);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).
                inflate(R.layout.dimension_item, parent, false);
        return new ResolutionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ResolutionItem item = mItems.get(position);
        TextView content = ((ResolutionHolder) holder).resolution;
        content.setText(item.label);

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelected = position;
                notifyDataSetChanged();
            }
        });

        if (position == mSelected) content.setSelected(true);
        else content.setSelected(false);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ResolutionHolder extends RecyclerView.ViewHolder {
        TextView resolution;

        ResolutionHolder(View itemView) {
            super(itemView);
            resolution = itemView.findViewById(R.id.resolution);
        }
    }

    public int getSelected() {
        return mSelected;
    }

    private static class ResolutionItem {
        String label;
        VideoEncoderConfiguration.VideoDimensions dimension;

        ResolutionItem(String label, VideoEncoderConfiguration.VideoDimensions dimension) {
            this.label = label;
            this.dimension = dimension;
        }
    }
}
