package com.p5m.me.utils;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.fxn.utility.Constants;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(1);
        return format.format(value);
       // String localLanguage=NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        //return localLanguage;
    }
    public static String numberConverter(double value)
    {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.CEILING);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(1);
        return format.format(value);

    }
    public static String numberConverter(double value,int decimalDigits)
    {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.CEILING);
        format.setMinimumFractionDigits(decimalDigits);
        format.setMaximumFractionDigits(decimalDigits);
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(value);

    }
    public static String promoConverter(double value,int decimalDigits)
    {
       /* NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(decimalDigits);
        format.setMaximumFractionDigits(decimalDigits);*/
        return String.format("%.2f", value);

    }
    public static String currencyConverter(long value)
    {
        String localLanguage=NumberFormat.getNumberInstance(Locale.getDefault()).format(value);
        return localLanguage;
    }

    public static String matchFitnessWord(String word, Context context){

        if(word.contains("INTERMEDIATE")){
            return context.getString(R.string.intermediate);
        }
        else if(word.contains("ADVANCED"))
            return context.getString(R.string.advanced);
        else
            return context.getString(R.string.basic);
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

    public static void setTextNormal(TextView textViewId, int numberOfViews, String viewString) {
        if(Constants.LANGUAGE==Locale.ENGLISH){
            textViewId.setText(Html.fromHtml(""+numberConverter(numberOfViews)+" "));
            textViewId.append(viewString);
        }
        else{
            textViewId.setText(viewString+" ");
            textViewId.append(Html.fromHtml(""+numberConverter(numberOfViews)+" "));
        }
    }

    public static void worldWideSetText(TextView textViewId,String startString , String endString){
        if(Constants.LANGUAGE==Locale.ENGLISH) {
            textViewId.setText(startString+"");
            textViewId.append(" "+endString);
        }
        else{
            textViewId.setText(startString+" ");
            textViewId.append(" "+endString);
        }
    }
}
