package com.p5m.me.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.data.ChannelModel;
import com.p5m.me.remote_config.RemoteConfigConst;

import java.util.List;

public class CommonUtillity {

    public static String getnerateUniqueToken(Context context){
       return RefrenceWrapper.getRefrenceWrapper(context).getDeviceId()+System.currentTimeMillis();
    }
    public static String getChannelName(String channelId){
        if(channelId!=null){
            try{
                String channelName = "";
                Gson g = new Gson();
                List<ChannelModel> p = g.fromJson(RemoteConfigConst.CHANNEL_LIST, new TypeToken<List<ChannelModel>>() {
                }.getType());
                for (ChannelModel model:p) {
                    if(model.getId().equalsIgnoreCase(channelId)){
                        channelName = model.getName();
                    }
                }

               return channelName;
            }catch (Exception e){
                e.printStackTrace();
                return "";
            }
        }


       return "";
    }
    // slide the view from below itself to the current position
    public static void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public static void slideDown(View view){
        view.setVisibility(View.GONE);
//        TranslateAnimation animate = new TranslateAnimation(
//                0,                 // fromXDelta
//                0,                 // toXDelta
//                0,                 // fromYDelta
//                view.getHeight()); // toYDelta
//        animate.setDuration(500);
//        animate.setFillAfter(true);
//        view.startAnimation(animate);

    }
    public static String formatDuration(final long millis) {
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = millis / (1000 * 60 * 60);

        StringBuilder b = new StringBuilder();
        if(hours>0){
            b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) :
                    String.valueOf(hours));
            b.append(":");
        }

        b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) :
                String.valueOf(minutes));
        b.append(":");
        b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) :
                String.valueOf(seconds));
        return b.toString();
    }

}
