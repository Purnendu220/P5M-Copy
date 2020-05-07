package com.p5m.me.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

public class OpenAppUtils {
    public static void openExternalApps(Context context,String platform,String url){
        switch (platform){
            case AppConstants.channelType.CHANNEL_YOUTUBE:
                openYoutube(context,url);
                break;
            case AppConstants.channelType.CHANNEL_FACEBOOK:
                openFacebook(context,url);
                break;
            case AppConstants.channelType.CHANNEL_INSTAGRAM:
                openInstagram(context,url);
                break;
            case AppConstants.channelType.CHANNEL_ZOOM:
                openZoom(context,url);
                break;
            case AppConstants.channelType.CHANNEL_SKYPE:
                openSkype(context,url);
                break;
            case AppConstants.channelType.CHANNEL_JITSI:
            case AppConstants.channelType.CHANNEL_GOOGLE:
            default:
                openBrowser(context,url);
                break;
        }
    }

    public static void openYoutube(Context context,String url){
        Intent intent = new Intent(
                Intent.ACTION_VIEW ,
                Uri.parse(url));
        intent.setComponent(new ComponentName("com.google.android.youtube","com.google.android.youtube.PlayerActivity"));
        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
        if (infos.size() > 0) {
            context.startActivity(intent);
        }else{
            openBrowser(context,url);
        }
    }
    public static void openZoom(Context context,String url){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
            else{
                openBrowser(context,url);
            }
        }catch (Exception e){
            openBrowser(context,url);

        }


    }
    public static void openFacebook(Context context,String url){
        try{
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(context,url);
            facebookIntent.setData(Uri.parse(facebookUrl));
            context.startActivity(facebookIntent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void openInstagram(Context context,String url){
        Uri uri = Uri.parse(url);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            context.startActivity(likeIng);
        } catch (Exception e) {
            openBrowser(context,url);
        }
    }
    public static void openSkype(Context myContext, String url) {
            try{


        if (!isSkypeClientInstalled(myContext)) {
            goToMarket(myContext);
            return;
        }

        // Create the Intent from our Skype URI.
        Uri skypeUri = Uri.parse(url);
        Intent myIntent = new Intent(Intent.ACTION_VIEW, skypeUri);

        // Restrict the Intent to being handled by the Skype for Android client only.
        myIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Initiate the Intent. It should never fail because you've already established the
        // presence of its handler (although there is an extremely minute window where that
        // handler can go away).
        myContext.startActivity(myIntent);
            }catch (Exception e){
                openBrowser(myContext,url);
            }
    }


    public static void openBrowser(Context context,String url){
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
        context.startActivity(intent);
    }
    public static boolean isSkypeClientInstalled(Context myContext) {
        PackageManager myPackageMgr = myContext.getPackageManager();
        try {
            myPackageMgr.getPackageInfo("com.skype.raider", PackageManager.GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e) {
            return (false);
        }
        return (true);
    }
    public static void goToMarket(Context myContext) {
        Uri marketUri = Uri.parse("market://details?id=com.skype.raider");
        Intent myIntent = new Intent(Intent.ACTION_VIEW, marketUri);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        myContext.startActivity(myIntent);

        return;
    }
    public static String getFacebookPageURL(Context context, String url) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + url;
            } else { //older versions of fb app
                return url;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return url; //normal web url
        }
    }
}
