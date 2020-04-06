package com.p5m.me.agorartc.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.p5m.me.agorartc.listeners.VideoViewEventListener;
import com.p5m.me.agorartc.stats.VideoStatusData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.utils.LogUtils;


import java.util.HashMap;


public class SmallVideoViewAdapter extends VideoViewAdapter {

    public SmallVideoViewAdapter(Context context, int exceptedUid, HashMap<Integer, SurfaceView> uids, VideoViewEventListener listener, ClassModel classModel) {
        super(context, exceptedUid, uids, listener,classModel);
    }

    @Override
    protected void customizedInit(HashMap<Integer, SurfaceView> uids, boolean force) {
        for (HashMap.Entry<Integer, SurfaceView> entry : uids.entrySet()) {
            //if (entry.getKey() != exceptedUid) {
                mUsers.add(new VideoStatusData(entry.getKey(), entry.getValue(), VideoStatusData.DEFAULT_STATUS, VideoStatusData.DEFAULT_VOLUME));
            //}
        }

        if (force || mItemWidth == 0 || mItemHeight == 0) {
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
            mItemWidth = outMetrics.widthPixels / 4;
            mItemHeight = outMetrics.heightPixels / 4;
        }
    }

    @Override
    public void notifyUiChanged(HashMap<Integer, SurfaceView> uids, int uidExcluded, HashMap<Integer, Integer> status, HashMap<Integer, Integer> volume) {
        mUsers.clear();

        for (HashMap.Entry<Integer, SurfaceView> entry : uids.entrySet()) {
            LogUtils.debug("notifyUiChanged " + entry.getKey() + " " + uidExcluded);

            entry.getValue().setZOrderMediaOverlay(false);
           // if (entry.getKey() != uidExcluded) {
                mUsers.add(new VideoStatusData(entry.getKey(), entry.getValue(), VideoStatusData.DEFAULT_STATUS, VideoStatusData.DEFAULT_VOLUME));
           // }
        }

        notifyDataSetChanged();
    }

    public int getExceptedUid() {
        return exceptedUid;
    }
}
