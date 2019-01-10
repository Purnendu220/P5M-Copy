package com.p5m.me.utils;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.p5m.me.R;
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
    public static String numberConverter(float value)
    {
        String localLanguage=NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        return localLanguage;
    }
    public static String numberConverter(double value)
    {
        String localLanguage=NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        return localLanguage;
    }
    public static String currencyConverter(long value)
    {
        String localLanguage=NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        return localLanguage;
    }

    public static String matchWord(String word, Context context){
        if(word.contains("MONTH")){
            return context.getString(R.string.month);
        }
        else return context.getString(R.string.week);
    }

    public static void setText(TextView textViewId, int numberOfViews, String viewString) {
        if(Constants.LANGUAGE==Locale.ENGLISH){
            textViewId.setText(Html.fromHtml("<b>"+numberConverter(numberOfViews)+"</b> "));
            textViewId.append(viewString);
        }
        else{
            textViewId.setText(viewString+" ");
            textViewId.append(Html.fromHtml("<b>"+numberConverter(numberOfViews)+"</b> "));
        }
    }
}
