package com.p5m.me.notifications;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.ProfileHeaderTabViewHolder;
import com.p5m.me.agorartc.activities.MainActivity;
import com.p5m.me.eventbus.EventBroadcastHelper;
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

import static com.p5m.me.utils.AppConstants.Tab.TAB_MY_MEMBERSHIP;

public class HandleNotificationDeepLink {
    public static Intent handleNotificationDeeplinking(Context context, String url) {
        Intent navigationIntent;
         try {
                if (url.contains("/classes/") || url.contains("/share/classes/")) {
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

                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                }

        } catch (Exception e) {
            e.printStackTrace();
            navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

        }


        return navigationIntent;

    }


}
