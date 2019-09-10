package com.p5m.me.storage;

import android.content.Context;

import com.p5m.me.data.City;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.RatingParamModel;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.DefaultSettingServer;
import com.p5m.me.data.main.User;
import com.p5m.me.ratemanager.RateAlarmReceiver;
import com.p5m.me.ratemanager.ScheduleAlarmManager;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TempStorage {

    private static int userId;
    public static String version;
    private static String authToken;
    private static List<ClassActivity> activities;
    private static List<City> cities;
    private static User user;
    private static DefaultSettingServer defaultSettingServer;

    private static List<ClassesFilter> filterList;


    public static List<City> getCities() {
        if (cities == null) {
            cities = MyPreferences.getInstance().getCities();
        }
        return cities;
    }

    public static void setCities(List<City> cities) {
        TempStorage.cities = cities;
    }

    public static List<ClassActivity> getActivities() {
        if (activities == null) {
            activities = MyPreferences.getInstance().getActivities();
        }
        return activities;
    }

    public static void setActivities(List<ClassActivity> activities) {
        TempStorage.activities = activities;
        MyPreferences.getInstance().saveActivities(activities);
    }
    public static void setRatingParams(List<RatingParamModel> ratingParamList) {
        MyPreferences.getInstance().saveRatingParams(ratingParamList);
    }
    public static  List<RatingParamModel> getRatingParams() {
       return MyPreferences.getInstance().getRatingParams();
    }
    public static DefaultSettingServer getDefault() {
        if (defaultSettingServer == null) {
            defaultSettingServer = MyPreferences.getInstance().getDefaultSettingServer();
        }
        return defaultSettingServer;
    }

    public static void setDefault(DefaultSettingServer defaultSettingServer) {
        TempStorage.defaultSettingServer = defaultSettingServer;
        MyPreferences.getInstance().saveDefaultSettingServer(defaultSettingServer);
    }

    public static String getAuthToken() {
        if (authToken == null) {
            authToken = MyPreferences.getInstance().getAuthToken();
        }
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        TempStorage.authToken = authToken;
        MyPreferences.getInstance().saveAuthToken(authToken);
    }

    public static void setLat(String Lat) {
        MyPreferences.getInstance().saveLat(Lat);
    }
    public static void setLng(String Lng) {
        MyPreferences.getInstance().saveLng(Lng);
    }

    public static String getLat() {
        return MyPreferences.getInstance().getLat();
    }

    public static String getLng() {
        return MyPreferences.getInstance().getLng();
    }

    public static boolean isOpenMembershipInfo() {
        return  MyPreferences.getInstance().isOpenMembershipInfo();
    }

    public static void setOpenMembershipInfo(boolean isOpen) {
        MyPreferences.getInstance().setOpenMembershipInfo(isOpen);
    }

    public static List<ClassesFilter> getFilters() {
        if (filterList == null) {
            filterList = MyPreferences.getInstance().getFilters();
            if (filterList == null) {
                filterList = new ArrayList<ClassesFilter>();
            }
        }
        return filterList;
    }

    public static void setFilterList(List<ClassesFilter> filterList) {
        TempStorage.filterList = filterList;
        MyPreferences.getInstance().saveFilters(filterList);
    }
 public static void setSavedClasses(ClassModel classModel,Context context){
     List<ClassModel> classList=  MyPreferences.getInstance().getJoinedClassList();
     if(classList!=null&&classList.size()>0){
        if(filterclassList(classList,classModel)==null||filterclassList(classList,classModel).size()==0){
            classList.add(classModel);
            MyPreferences.getInstance().saveJoinedClassList(classList);
            ScheduleAlarmManager.setReminder(context,RateAlarmReceiver.class,classModel);
            LogUtils.debug("Class finish alert set for the "+classModel.getTitle());
            return;
            }
     }
     else{
       classList=new ArrayList<>();
       classList.add(classModel);
       MyPreferences.getInstance().saveJoinedClassList(classList);
         ScheduleAlarmManager.setReminder(context,RateAlarmReceiver.class,classModel);
         LogUtils.debug("Class finish alert set for the "+classModel.getTitle()+" else");

         return;
     }

     }



 public static void removeSavedClass(int classSessionId,Context context){
        try{
            List<ClassModel> classList=  MyPreferences.getInstance().getJoinedClassList();
            List<ClassModel> classListToRemove=new ArrayList<>();
            if(classList!=null&&classList.size()>0){
                for (ClassModel model:classList) {
                    if(model.getClassSessionId() == classSessionId){
                        classListToRemove.add(model);
                    }
                }
                classList.removeAll(classListToRemove);
                MyPreferences.getInstance().saveJoinedClassList(classList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

         ScheduleAlarmManager.cancelReminder(context,RateAlarmReceiver.class,classSessionId);
         rescheduleAlarms(context);
         return;


 }
 public static void rescheduleAlarms(Context context){
     try{
            List<ClassModel> classList=  MyPreferences.getInstance().getJoinedClassList();
            if(classList!=null&&classList.size()>0){
                for (ClassModel model:classList) {
                   Date date=DateUtils.getClassDate(model.getClassDate(),model.getToTime());
                   if(date.after(new Date())){
                       ScheduleAlarmManager.setReminder(context,RateAlarmReceiver.class,model);
                       }
                }
            } }catch (Exception e){
e.printStackTrace();
        }
 }
    public static void removeSavedClassOnly(int classSessionId,Context context){
        try{
            List<ClassModel> classList=  MyPreferences.getInstance().getJoinedClassList();
            List<ClassModel> classListToRemove=new ArrayList<>();
            if(classList!=null&&classList.size()>0){
                for (ClassModel model:classList) {
                    if(model.getClassSessionId()==classSessionId){
                        classListToRemove.remove(model);
                    }
                }
                classList.removeAll(classListToRemove);
                MyPreferences.getInstance().saveJoinedClassList(classList);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return;


    }
 public static void removeAllSavedClasses(List<ClassModel> classList, Context context){
     List<ClassModel> classListEmpty=new ArrayList<>();
     if(classList!=null && classList.size()>0){
         for (ClassModel model:classList) {
             ScheduleAlarmManager.cancelReminder(context,RateAlarmReceiver.class,model.getClassSessionId());
         }
     }
         MyPreferences.getInstance().saveJoinedClassList(classListEmpty);
    }
 public static List<ClassModel>  getSavedClasses(){
     return  MyPreferences.getInstance().getJoinedClassList();
 }


    public static User getUser() {
        if (user == null) {
            user = MyPreferences.getInstance().getUser();
        }

        return user;
    }

    public static void setUser(Context context, User user) {
        TempStorage.user = user;
        MyPreferences.getInstance().saveUser(user);
    }
    public static List<ClassModel> filterclassList(List<ClassModel> classList,ClassModel classModel){
        try{
            List<ClassModel> filteredClassList = new ArrayList<>();
            for (ClassModel model:classList) {
                if(model.getClassSessionId()==classModel.getClassSessionId()){
                    filteredClassList.add(model);
                }
            }

            return filteredClassList;
        }catch (Exception e){
           return null;
        }

    }


}
