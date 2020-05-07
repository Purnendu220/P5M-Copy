package com.p5m.me.utils;

import android.content.Context;

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


       return "On P5M";
    }

}
