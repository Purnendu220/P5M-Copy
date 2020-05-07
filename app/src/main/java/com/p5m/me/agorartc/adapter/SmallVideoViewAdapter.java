package com.p5m.me.agorartc.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.agorartc.listeners.VideoViewEventListener;
import com.p5m.me.agorartc.stats.VideoStatusData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.utils.LogUtils;


import java.util.HashMap;


public class SmallVideoViewAdapter extends VideoViewAdapter {

    public SmallVideoViewAdapter(Context context, int exceptedUid, HashMap<Integer, VideoStatusData> uids, VideoViewEventListener listener, ClassModel classModel) {
        super(context, exceptedUid, uids, listener,classModel);
    }

    @Override
    protected void customizedInit(HashMap<Integer, VideoStatusData> uids, boolean force) {
        for (HashMap.Entry<Integer, VideoStatusData> entry : uids.entrySet()) {
            //if (entry.getKey() != exceptedUid) {
                mUsers.add(entry.getValue());
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
    public void notifyUiChanged(HashMap<Integer, VideoStatusData> uids, int uidExcluded, HashMap<Integer, Integer> status, HashMap<Integer, Integer> volume) {
        mUsers.clear();

        for (HashMap.Entry<Integer, VideoStatusData> entry : uids.entrySet()) {
            LogUtils.debug("notifyUiChanged " + entry.getKey() + " " + uidExcluded);
            entry.getValue().mView.setZOrderMediaOverlay(false);
                mUsers.add(entry.getValue());
        }

        notifyDataSetChanged();
    }
    public void notifyUserAdded(VideoStatusData dataAdded) {
        boolean isDataAlreadyadded=false;
        for (VideoStatusData data:mUsers) {
            if(data.mUid==dataAdded.mUid){
                isDataAlreadyadded = true;
            }
        }
        if(!isDataAlreadyadded){
            dataAdded.mView.setZOrderMediaOverlay(false);

            mUsers.add(dataAdded);
            notifyDataSetChanged();
        }

        }


        public void notifyUserRemoved(int uid) {
        int indexToBeRemoved = -1;
        for (VideoStatusData data:mUsers) {
           if(data.mUid==uid){
               indexToBeRemoved = mUsers.indexOf(data);
           }
        }
            if(indexToBeRemoved>-1){
                mUsers.remove(indexToBeRemoved);
            }
            notifyDataSetChanged();
    }



    public int getExceptedUid() {
        return exceptedUid;
    }
}
