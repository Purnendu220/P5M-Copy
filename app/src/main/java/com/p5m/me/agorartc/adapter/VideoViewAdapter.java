package com.p5m.me.agorartc.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.p5m.me.R;
import com.p5m.me.agorartc.listeners.OnDoubleTapListener;
import com.p5m.me.agorartc.listeners.VideoViewEventListener;
import com.p5m.me.agorartc.stats.VideoStatusData;
import com.p5m.me.agorartc.viewholder.VideoUserStatusHolder;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class VideoViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    protected final LayoutInflater mInflater;
    protected final Context mContext;

    protected final ArrayList<VideoStatusData> mUsers;

    protected final VideoViewEventListener mListener;

    protected int exceptedUid;

    protected ClassModel classModel;

    public VideoViewAdapter(Context context, int exceptedUid, HashMap<Integer, VideoStatusData> uids, VideoViewEventListener listener,ClassModel classModel) {
        mContext = context;
        mInflater = ((Activity) context).getLayoutInflater();

        mListener = listener;

        mUsers = new ArrayList<>();

        this.exceptedUid = exceptedUid;

        this.classModel = classModel;

        init(uids);
    }

    protected int mItemWidth;
    protected int mItemHeight;

    private void init(HashMap<Integer, VideoStatusData> uids) {
        mUsers.clear();

        customizedInit(uids, true);
    }

    protected abstract void customizedInit(HashMap<Integer, VideoStatusData> uids, boolean force);

    public abstract void notifyUiChanged(HashMap<Integer, VideoStatusData> uids, int uidExcluded, HashMap<Integer, Integer> status, HashMap<Integer, Integer> volume);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("VideoViewAdapter", "onCreateViewHolder " + viewType);

        View v = mInflater.inflate(R.layout.video_view_container, parent, false);
        v.getLayoutParams().width = mItemWidth;
        v.getLayoutParams().height = mItemWidth;
        v.setElevation(5);
        v.setBackgroundResource(R.drawable.tags_rounded_corners);
       return new VideoUserStatusHolder(v);
    }

    protected final void stripSurfaceView(SurfaceView view) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VideoUserStatusHolder myHolder = ((VideoUserStatusHolder) holder);

        final VideoStatusData user = mUsers.get(position);

        Log.d("VideoViewAdapter", "onBindViewHolder " + position + " " + user + " " + myHolder + " " + myHolder.itemView);

        LogUtils.debug("onBindViewHolder " + position + " " + user + " " + myHolder + " " + myHolder.itemView);

        FrameLayout holderView = (FrameLayout) myHolder.itemView;

        if (holderView.getChildCount() == 0) {
            SurfaceView target = user.mView;
            stripSurfaceView(target);
            target.setZOrderMediaOverlay(true);
            holderView.addView(target, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if(Helper.isTrainerOrGym(classModel,user)||user.mUid == 0){
                TextView txt = new TextView(mContext);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,0,0);
                txt.setLayoutParams(params);
                String textTitle = user.mUid == 0?mContext.getResources().getString(R.string.you):mContext.getResources().getString(R.string.trainer);
                txt.setText(textTitle);
                txt.setBackgroundResource(R.drawable.live_name_board_bg);
                txt.setTextColor(Color.WHITE);
                int paddingDp = 10;
                float density = mContext.getResources().getDisplayMetrics().density;
                int paddingPixel = (int)(paddingDp * density);
                txt.setPadding(paddingPixel,0,paddingPixel,0);
                holderView.addView(txt);
            }

        }

        holderView.setOnTouchListener(new OnDoubleTapListener(mContext) {
            @Override
            public void onDoubleTap(View view, MotionEvent e) {
                if (mListener != null) {
                    mListener.onItemDoubleClick(view, user);
                }
            }

            @Override
            public void onSingleTapUp() {
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("VideoViewAdapter", "getItemCount " + mUsers.size());
        return mUsers.size();
    }

    @Override
    public long getItemId(int position) {
        VideoStatusData user = mUsers.get(position);

        SurfaceView view = user.mView;
        if (view == null) {
            throw new NullPointerException("SurfaceView destroyed for user " + (user.mUid & 0xFFFFFFFFL) + " " + user.mStatus + " " + user.mVolume);
        }

        return (String.valueOf(user.mUid) + System.identityHashCode(view)).hashCode();
    }
}
