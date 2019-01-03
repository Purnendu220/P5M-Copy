package com.p5m.me.utils;

import android.util.Log;

import com.p5m.me.fxn.utility.Constants;

import java.text.NumberFormat;
import java.util.Locale;

public class LanguageUtils {
    public static String getLocalLanguage(){
        String language=Locale.getDefault().getLanguage();
        Log.d("language of app", "langg "+language);
        if(language.equals("ar")){
            Log.d("lang_arabic", language);
            Constants.LANGUAGE=Locale.getDefault();
        }
       else{
            language="en";
            Constants.LANGUAGE=Locale.ENGLISH;
            //send_msg_text.
        }
        return language;
    }

    public static String numberConverter(int value)
    {
        String localLanguage=NumberFormat.getInstance(Locale.getDefault()).format(value);
        return localLanguage;
    }
    public static String currencyConverter(float value)
    {
        String localLanguage=NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        return localLanguage;
    }
    public static String currencyConverter(double value)
    {
        String localLanguage=NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        return localLanguage;
    }
    public static String currencyConverter(long value)
    {
        String localLanguage=NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        return localLanguage;
    }
}
