package www.gymhop.p5m.utils;

import android.util.Log;

import www.gymhop.p5m.MyApplication;

/**
 * Created by MyU10 on 1/18/2017.
 */

public class LogUtils {
    public static void networkError(String msg) {
        log("Network", "Error" + msg);
    }

    public static void networkSuccess(String msg) {
        log("Network", "Success " + msg);
    }

    public static void xmppDebug(String msg) {
        log("XmppVarun", "Varun XMPP : " + msg);
    }

    public static void database(String msg) {
        log("DataBaseVarun", "SQL: " + msg);
    }

    public static void debug(String msg) {
        log("VarunDebug", msg);
    }

    private static void log(String tag, String msg) {
        if (MyApplication.SHOW_LOG) {
            Log.d(tag, msg);
        }
    }

}
