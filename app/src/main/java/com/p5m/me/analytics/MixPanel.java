package com.p5m.me.analytics;

/**
 * Created by Varun John on 6/18/2018.
 */

public class MixPanel {

//    private static HandlerThread handlerThread;
//    private static Handler handlerBg;
//
//    public static void setup(Context context) {
//
//        if (handlerThread != null) {
//            handlerThread = new HandlerThread("HandlerThreadMixPanel");
//            handlerThread.start();
//        }
//
//        if (handlerBg != null) {
//            handlerBg = new Handler(handlerThread.getLooper());
//        }
//
//        if (MyPreferences.getInstance().isLogin()) {
//            if (MyApp.mixPanel != null) {
//                MyApp.mixPanel.identify(MyApp.mixPanel.getDistinctId());
//            }
//        }
//
//        try {
//            JSONObject props = new JSONObject();
//            props.put("Source", "Android");
//            MyApp.mixPanel.registerSuperProperties(props);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.exception(e);
//        }
//    }
}
