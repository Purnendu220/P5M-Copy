package com.p5m.me.notifications;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.ProfileHeaderTabViewHolder;
import com.p5m.me.agorartc.activities.MainActivity;
import com.p5m.me.data.City;
import com.p5m.me.data.CityLocality;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.PriceModel;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.GymDataModel;
import com.p5m.me.data.main.StoreApiModel;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivity;
import com.p5m.me.view.activity.Main.EditProfileActivity;
import com.p5m.me.view.activity.Main.GymProfileActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.Main.NotificationActivity;
import com.p5m.me.view.activity.Main.PackageLimitsActivity;
import com.p5m.me.view.activity.Main.SettingActivity;
import com.p5m.me.view.activity.Main.SettingNotification;
import com.p5m.me.view.activity.Main.TrainerProfileActivity;
import com.p5m.me.view.activity.Main.Trainers;
import com.p5m.me.view.activity.Main.TransactionHistoryActivity;
import com.p5m.me.view.activity.Main.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import static com.p5m.me.utils.AppConstants.Tab.TAB_MY_MEMBERSHIP;

public class HandleNotificationDeepLink {
    public static Intent handleNotificationDeeplinking(Context context, String url) {
        Intent navigationIntent;
         try {


             if(url.contains("/classes?type")){
                 String[] stringlist = url.split("\\?");
                 String[] paramList = stringlist[1].split("&");
                 String type = paramList[0].split("=")[1];
                 String id = paramList[1].split("=")[1];
                 applyFilter(type,id,context);
                 navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);


             }

            else if (url.contains("/classes/") || url.contains("/share/classes/")) {
                    String classId = null;
                    String[] stringlist = url.split("/classes/");
                    if (stringlist != null && stringlist.length > 1) {
                        classId = stringlist[1].split("/")[0];
                    }
                    try {
                        navigationIntent = ClassProfileActivity.createIntent(context, Integer.parseInt(classId), AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);


                    } catch (Exception e) {
                        e.printStackTrace();
                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                    }

                } else if (url.contains("/share/gym/") || url.contains("/gym/")) {
                    String gymId = null;
                    String[] stringlist = url.split("/gym/");
                    if (stringlist != null && stringlist.length > 1) {
                        gymId = stringlist[1].split("/")[0];
                    }
                    try {
                        navigationIntent = GymProfileActivity.createIntent(context, Integer.parseInt(gymId), AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);


                    } catch (Exception e) {
                        e.printStackTrace();
                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                    }

                } else if (url.contains("/share/trainer/") || url.contains("/trainer/")) {
                    String trainerId = null;
                    String[] stringlist = url.split("/trainer/");
                    if (stringlist != null && stringlist.length > 1) {
                        trainerId = stringlist[1].split("/")[0];
                    }
                    try {
                        navigationIntent = TrainerProfileActivity.createIntent(context, Integer.parseInt(trainerId), AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);


                    } catch (Exception e) {
                        e.printStackTrace();
                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                    }
                } else if (url.contains("/classes")) {
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);


                } else if (url.contains("/trainers")) {
//                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_TRAINER, 0);
                    navigationIntent = Trainers.createIntent(context);


                } else if (url.contains("/userschedule")) {
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_SCHEDULE, 0);


                } else if (url.contains("/profile?type=favTrainer")) {
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_MY_PROFILE, 0, ProfileHeaderTabViewHolder.TAB_1);
                    EventBroadcastHelper.bannerUrlHandler(ProfileHeaderTabViewHolder.TAB_1);

                } else if (url.contains("/profile?type=finishedClasses")) {
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_MY_PROFILE, 0, ProfileHeaderTabViewHolder.TAB_2);
                    EventBroadcastHelper.bannerUrlHandler(ProfileHeaderTabViewHolder.TAB_2);


                } else if (url.contains("/profile")) {
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_MY_PROFILE, 0);


                } else if (url.contains("/editprofile")) {
                    navigationIntent = EditProfileActivity.createIntent(context);


                } else if (url.contains("/settings/membership")) {

                    navigationIntent = HomeActivity.showMembership(context, TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);


                } else if (url.contains("/settings/transaction")) {
                    navigationIntent = TransactionHistoryActivity.createIntent(context, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);


                } else if (url.contains("/settings/notification")) {
                    navigationIntent = SettingNotification.createIntent(context);

                } else if (url.contains("/settings/aboutus") ||
                        url.contains("/aboutus")) {
                    navigationIntent = SettingActivity.createIntent(context, AppConstants.Tab.OPEN_ABOUT_US);

                } else if (url.contains("/settings/notifications")) {
                    navigationIntent = NotificationActivity.createIntent(context);

                } else if (url.contains("/notifications")) {
                    navigationIntent = NotificationActivity.createIntent(context);

                } else if (url.contains("/settings")) {
                    navigationIntent = HomeActivity.showMembership(context, TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);
                } else if (url.contains("/searchresults/")) {
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                } else if (url.contains("/visit_gym/")) {

                    try {
                        navigationIntent = PackageLimitsActivity.createIntent(context);

                    } catch (Exception e) {
                        e.printStackTrace();
                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                    }

                }
                else if (url.contains("/videobroadcast/")) {
                    String sessionId = null;
                    String userId = null;
                    String[] stringlist = url.split("/videobroadcast/");
                    if (stringlist != null && stringlist.length > 1) {
                        sessionId = stringlist[1].split("/")[0];
                        userId = stringlist[1].split("/")[1];

                    }
                    try {
                        if(Integer.parseInt(userId) == TempStorage.getUser().getId()){
                        navigationIntent = MainActivity.createIntent(context,sessionId,userId);
                        }
                        else{
                            ToastUtils.show(context,context.getResources().getString(R.string.this_class_is_not_for_you));
                            navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                    }

                }
                else {

                    navigationIntent = WebViewActivity.createIntent(context, url);

                }

        } catch (Exception e) {
            e.printStackTrace();
            navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

        }


        return navigationIntent;

    }

    private static void applyFilter(String type,String id,Context context){
         List<ClassActivity> activities;
         List<GymDataModel> gymList;
         List<City> cities;
         List<Filter.FitnessLevel> fitnessLevelList;
         List<PriceModel> priceModelList = null;
         List<ClassesFilter> classesFilters = new ArrayList<>();

        switch (type){
            case "activity":
                try{
                    activities =   TempStorage.getActivities();
                    ClassActivity selected = null;
                    for (ClassActivity activty:activities) {
                        if(activty.getId()==Integer.parseInt(id)){
                            selected = activty;
                        }
                    }
                    if(selected!=null){
                        ClassesFilter classesFilter = new ClassesFilter(selected.getId() + "", true, "ClassActivity", selected.getName(), R.drawable.filter_activity, ClassesFilter.TYPE_ITEM);
                        classesFilter.setObject(selected);
                        classesFilters.add(classesFilter);

                        TempStorage.setFilterList(classesFilters);
                        EventBroadcastHelper.sendNewFilterSet();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



                break;
            case "locality":
                cities = TempStorage.getCities();
                CityLocality selectedlocality=null;
                try{
                    for (City city:cities) {
                        for (CityLocality locality:city.getLocality()) {
                            if(locality.getId()==Integer.parseInt(id)){
                                selectedlocality = locality;
                            }
                        }
                    }

                    if(selectedlocality!=null){
                        ClassesFilter filter = new ClassesFilter<CityLocality>(selectedlocality.getId() + "", true, "CityLocality", selectedlocality.getName(), 0, ClassesFilter.TYPE_ITEM);
                        filter.setObject(selectedlocality);
                        classesFilters.add(filter);

                        TempStorage.setFilterList(classesFilters);
                        EventBroadcastHelper.sendNewFilterSet();
                    }




                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "gym":
                gymList = TempStorage.getGymList();
                GymDataModel selectedGym=null;
                for (GymDataModel model:gymList) {
                    if(model.getId()==Integer.parseInt(id)){
                        selectedGym=model;
                    }
                }
                if(selectedGym!=null){
                    ClassesFilter classesFilter = new ClassesFilter(selectedGym.getId() + "", true, "Gym", selectedGym.getStudioName(), 0, ClassesFilter.TYPE_ITEM);
                    classesFilter.setObject(selectedGym);
                    classesFilters.add(classesFilter);
                    TempStorage.setFilterList(classesFilters);
                    EventBroadcastHelper.sendNewFilterSet();
                }


                break;
            case "priceModel":
                PriceModel selected = null;

                try {
                    priceModelList = new Gson().fromJson(RemoteConfigure.getFirebaseRemoteConfig(context).getRemoteConfigValue(RemoteConfigConst.PRICE_MODEL), new TypeToken<List<PriceModel>>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(priceModelList!=null&&!priceModelList.isEmpty()){
                    for (PriceModel model:priceModelList) {
                        if(id.equalsIgnoreCase(model.getValue())){
                            selected = model;
                        }
                    }
                    if(selected!=null){
                    ClassesFilter   filter = new ClassesFilter("", true, "PriceModel", selected.getName(), R.drawable.multiple_users_grey_fill, ClassesFilter.TYPE_ITEM);
                    filter.setObject(selected);
                        classesFilters.add(filter);
                        TempStorage.setFilterList(classesFilters);
                        EventBroadcastHelper.sendNewFilterSet();
                    }
                }
                break;
            case "time":
                ClassesFilter filter=null;
                if(id.equalsIgnoreCase("MORNING")){
                     filter = new ClassesFilter<Filter.Time>("", true, "Time",context.getString(R.string.morning), 0, ClassesFilter.TYPE_ITEM);
                      filter.setObject(new Filter.Time("MORNING", context.getString(R.string.morning)));
                }
                if(id.equalsIgnoreCase("AFTERNOON")){
                    filter = new ClassesFilter<Filter.Time>("", true, "Time",context.getString(R.string.after_Noon), 0, ClassesFilter.TYPE_ITEM);
                    filter.setObject(new Filter.Time("AFTERNOON", context.getString(R.string.after_Noon)));

                }
                if(id.equalsIgnoreCase("EVENING")){
                    filter = new ClassesFilter<Filter.Time>("", true, "Time",context.getString(R.string.evening), 0, ClassesFilter.TYPE_ITEM);
                    filter.setObject(new Filter.Time("EVENING", context.getString(R.string.evening)));

                }
                if(filter!=null){
                    classesFilters.add(filter);
                    TempStorage.setFilterList(classesFilters);
                    EventBroadcastHelper.sendNewFilterSet();
                }
                break;
            case "level":
                try{
                    Filter.FitnessLevel selectedLevel = null;
                    fitnessLevelList = new Gson().fromJson(Helper.getFitnessLevelFromAsset(context), new TypeToken<List<Filter.FitnessLevel>>() {
                        }.getType());


                    if(fitnessLevelList!=null&&!fitnessLevelList.isEmpty()){
                        for (Filter.FitnessLevel model:fitnessLevelList) {
                            if(model.getLevel().equalsIgnoreCase(id)){
                                selectedLevel = model;
                            }
                        }

                    }
                    if(selectedLevel!=null){
                        ClassesFilter classesFilter = new ClassesFilter(selectedLevel.getId() + "", true, "FitnessLevel", selectedLevel.getName(), 0, ClassesFilter.TYPE_ITEM);
                        classesFilter.setObject(selectedLevel);
                        classesFilters.add(classesFilter);
                        TempStorage.setFilterList(classesFilters);
                        EventBroadcastHelper.sendNewFilterSet();
                    }



                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }


    }

}
