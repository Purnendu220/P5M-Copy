package com.p5m.me.utils;

import android.util.Log;

import java.util.Locale;

public class LanguageUtils {
    public static String getLocalLanguage(){
        String language=Locale.getDefault().getLanguage();
        Log.d("language of app", "langg "+language);
        if(language.equals("ar")){
            Log.d("lang_arabic", language);
        }
       else{
            language="en";
            //send_msg_text.
        }
        return language;
    }
}
