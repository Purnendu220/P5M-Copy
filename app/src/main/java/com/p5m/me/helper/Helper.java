package com.p5m.me.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.BuildConfig;
import com.p5m.me.MyApp;
import com.p5m.me.R;
import com.p5m.me.agorartc.stats.VideoStatusData;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.GymBranchDetail;
import com.p5m.me.data.main.GymDetailModel;
import com.p5m.me.data.main.MediaModel;
import com.p5m.me.data.main.TrainerDetailModel;
import com.p5m.me.data.main.TrainerModel;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.KeyboardUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.LoginRegister.ContinueUser;
import com.p5m.me.view.activity.LoginRegister.InfoScreen;
import com.p5m.me.view.activity.Main.LocationActivity;
import com.p5m.me.view.custom.GalleryActivity;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Varun John on 4/20/2018.
 */

public class Helper {

    public static void setupEditTextFocusHideKeyboard(EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    KeyboardUtils.close(v, v.getContext());
                } else {
                    KeyboardUtils.open(v, v.getContext());
                }
            }
        });
    }

    public static String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    public static void setupErrorWatcher(EditText editText, final TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textInputLayout.setError("");
            }
        });
    }

    public static String getCategoryListFromClassActivity(List<ClassActivity> list) {
        String name = "";

        if (list != null && !list.isEmpty()) {

            try {
                for (int index = 0; index < list.size(); index++) {
                    String value = list.get(index).getName();
                    if (!name.contains(value)) {
                        name = index == 0 ? (name += value) : (name += ", " + value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
        }

        if (name.isEmpty()) {
            name = "No activities";
        }

        return name;
    }

    public static String getCategoryList(List<String> list) {
        String name = "";

        if (list != null && !list.isEmpty()) {

            try {
                for (int index = 0; index < list.size(); index++) {
                    String value = list.get(index);
                    if (!name.contains(value)) {
                        name = index == 0 ? (name += value) : (name += ", " + value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
        }

        if (name.isEmpty()) {
            name = "No activities";
        }

        return name;
    }

    public static String getBranchList(List<GymBranchDetail> list) {
        String name = "";
        if (list != null && !list.isEmpty()) {
            try {
                for (int index = 0; index < list.size(); index++) {
                    String value = list.get(index).getBranchName();
                    if (!name.contains(value)) {
                        name = index == 0 ? (name += value) : (name += ", " + value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
        }
        return name;
    }

    public static void setJoinButton(Context context, Button buttonJoin, ClassModel model) {

        if (model.isUserJoinStatus()) {

            buttonJoin.setText(RemoteConfigConst.BOOKED_VALUE);
            RemoteConfigSetUp.setBackgroundColor(buttonJoin, RemoteConfigConst.BOOKED_COLOR_VALUE, context.getResources().getColor(R.color.theme_booked));


        } else if (model.getAvailableSeat() == 0) {

            if(model.getWishType()!=null) {
                if(model.getWishType().equalsIgnoreCase(AppConstants.ApiParamKey.WAITLIST)) {
                    buttonJoin.setText(RemoteConfigConst.WAITLISTED_VALUE);
                    RemoteConfigSetUp.setBackgroundColor(buttonJoin, RemoteConfigConst.BOOKED_COLOR_VALUE, context.getResources().getColor(R.color.theme_booked));

                }
                else{
                    buttonJoin.setText(RemoteConfigConst.WAITLIST_VALUE);
                    RemoteConfigSetUp.setBackgroundColor(buttonJoin, RemoteConfigConst.BOOK_COLOR_VALUE, context.getResources().getColor(R.color.theme_book));
                }
            }
            else{
                buttonJoin.setText(RemoteConfigConst.WAITLIST_VALUE);
                RemoteConfigSetUp.setBackgroundColor(buttonJoin, RemoteConfigConst.BOOK_COLOR_VALUE, context.getResources().getColor(R.color.theme_book));
            }

        } else {

            buttonJoin.setText(RemoteConfigConst.BOOK_VALUE);
            RemoteConfigSetUp.setBackgroundColor(buttonJoin, RemoteConfigConst.BOOK_COLOR_VALUE, context.getResources().getColor(R.color.theme_book));


        }
    }

    public static void setJoinStatusProfile(Context context, TextView view, TextView view1, ClassModel model) {
        if (model.isExpired()) {
//            view.setText(context.getString(R.string.expired));
//            view.setBackgroundResource(R.drawable.theme_bottom_text_button_full);
            view.setText(RemoteConfigConst.SESSION_EXPIRED_VALUE);
            RemoteConfigSetUp.setBackgroundColor(view, RemoteConfigConst.SESSION_EXPIRED_COLOR_VALUE, context.getResources().getColor(R.color.theme_full));
            view.setEnabled(false);
            view1.setVisibility(View.GONE);
        } else if (model.isUserJoinStatus()) {
//            view.setText(context.getString(R.string.booked));
            view.setText(RemoteConfigConst.BOOKED_VALUE);
          //  view.setBackgroundColor(context.getResources().getColor(R.color.theme_booked));
            RemoteConfigSetUp.setBackgroundColor(view, RemoteConfigConst.BOOKED_COLOR_VALUE, context.getResources().getColor(R.color.theme_booked));

            view.setEnabled(false);
            view1.setVisibility(View.GONE);

        } else if (model.getAvailableSeat() == 0) {
//            view.setText(context.getString(R.string.full));

            if(model.getWishType()!=null) {
                if(model.getWishType().equalsIgnoreCase(AppConstants.ApiParamKey.WAITLIST)) {
                    view.setText(RemoteConfigConst.WAITLISTED_VALUE);
                    RemoteConfigSetUp.setBackgroundColor(view, RemoteConfigConst.BOOKED_COLOR_VALUE, context.getResources().getColor(R.color.theme_booked));
                }
                else{
                    view.setText(RemoteConfigConst.WAITLIST_VALUE);
                    RemoteConfigSetUp.setBackgroundColor(view, RemoteConfigConst.BOOK_COLOR_VALUE, context.getResources().getColor(R.color.theme_book));

                }
            }
            else{
                view.setText(RemoteConfigConst.WAITLIST_VALUE);
                RemoteConfigSetUp.setBackgroundColor(view, RemoteConfigConst.BOOK_COLOR_VALUE, context.getResources().getColor(R.color.theme_book));

            }

            view1.setVisibility(View.GONE);
        }
        else if (model.getAvailableSeat() < 2) {
            view1.setVisibility(View.GONE);
            view.setText(RemoteConfigConst.BOOK_IN_CLASS_VALUE);
            RemoteConfigSetUp.setBackgroundColor(view, RemoteConfigConst.BOOK_IN_CLASS_COLOR_VALUE, context.getResources().getColor(R.color.colorAccent));

        }
        else {
//            view.setText(context.getString(R.string.reserve_class));
            view.setText(RemoteConfigConst.BOOK_IN_CLASS_VALUE);

//            view.setBackgroundResource(R.drawable.theme_bottom_text_button_book);
            RemoteConfigSetUp.setBackgroundColor(view, RemoteConfigConst.BOOK_IN_CLASS_COLOR_VALUE, context.getResources().getColor(R.color.colorAccent));

            view.setEnabled(true);
            view1.setVisibility(View.VISIBLE);
//            view1.setText(context.getString(R.string.reserve_class_with_friend));

//            view1.setBackgroundResource(R.drawable.theme_bottom_text_button_book);
           // RemoteConfigSetUp.setBackgroundColor(view1, RemoteConfigConst.BOOK_WITH_FRIEND_COLOR_VALUE, context.getResources().getColor(R.color.colorAccent));
            view1.setText(RemoteConfigConst.BOOK_WITH_FRIEND_VALUE);
            view1.setEnabled(true);

        }
    }

    public static void setFavButtonTemp(Context context, Button buttonFav, boolean isFollow) {
        setFavButton(context, buttonFav, isFollow);
    }

    public static void setFavButtonTemp(Context context, ImageButton buttonFav, boolean isFollow) {
        setFavButton(context, buttonFav, isFollow);
    }

    public static void setFavButton(Context context, Button buttonFav, TrainerModel model) {
        setFavButton(context, buttonFav, model.isIsfollow());
    }

    public static void setFavButton(Context context, ImageButton buttonFav, TrainerModel model) {
        setFavButtonGray(context, buttonFav, model.isIsfollow());
    }

    public static void setFavButton(Context context, ImageButton buttonFav, TrainerDetailModel model) {
        setFavButton(context, buttonFav, model.isIsfollow());
    }

    public static void setFavButton(Context context, Button buttonFav, GymDetailModel model) {
        setFavButton(context, buttonFav, model.isIsfollow());
    }

    private static void setFavButton(Context context, Button buttonFav, boolean isFollow) {
        if (isFollow) {
            buttonFav.setText(context.getString(R.string.favorited));
            buttonFav.setTextColor(ContextCompat.getColor(context, R.color.white));
            buttonFav.setBackgroundResource(R.drawable.join_rect);
        } else {
            buttonFav.setText(context.getString(R.string.favourite));
            buttonFav.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
            buttonFav.setBackgroundResource(R.drawable.button_white);
        }
    }

    private static void setFavButton(Context context, ImageButton buttonFav, boolean isFollow) {
        if (isFollow) {
            buttonFav.setImageResource(R.drawable.heart_fav);
        } else {
            buttonFav.setImageResource(R.drawable.heart_white_unfav);

        }
    }

    private static void setFavButtonGray(Context context, ImageButton buttonFav, boolean isFollow) {

        if (isFollow) {
            buttonFav.setImageResource(R.drawable.heart_fav);
        } else {
            buttonFav.setImageResource(R.drawable.heart_unfav);

        }
    }

    public static void openImageListViewer(Context context, List<MediaModel> list, int position, String viewholderType) {

        List<String> images = new ArrayList<>(list.size());
        for (MediaModel mediaModel : list) {
            images.add(mediaModel.getMediaUrl());
        }

        GalleryActivity.openActivity(context, null, null, position, images, viewholderType);


//        Fresco.initialize(context);
//
//        GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
//                .setFailureImage(R.drawable.class_holder)
////                .setPlaceholderImage(R.drawable.loading)
//                ;
//
//        new ImageViewer.Builder<>(context, list)
//                .setStartPosition(position)
//                .hideStatusBar(false)
//                .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
//                .setFormatter(new ImageViewer.Formatter<MediaModel>() {
//                    @Override
//                    public String format(MediaModel model) {
//                        return model.getMediaUrl();
//                    }
//                })
//                .show();
    }

    public static void openImageViewer(Context context, Activity activity, View sharedElement, String url, String viewholderType) {

        if (url == null || url.isEmpty()) {
            return;
        }

        List<String> images = new ArrayList<>(1);
        images.add(url);

        GalleryActivity.openActivity(context, activity, sharedElement, 0, images, viewholderType);

//        Fresco.initialize(context);
//
//        GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
//                .setFailureImage(R.drawable.class_holder)
////                .setPlaceholderImage(R.drawable.loading)
//                ;
//
//        new ImageViewer.Builder<>(context, new String[]{url})
//                .hideStatusBar(false)
//                .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
//                .show();
    }

    public static void showPopMenu(Context context, View view, PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.action_profile, popup.getMenu());

        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            LogUtils.exception(e);
        }

        popup.setOnMenuItemClickListener(onMenuItemClickListener);
        popup.show();
    }

    public static void openMap(Context context, Double latitude, Double longitude, String label) {
        if (longitude == null || latitude == null) {
            return;
        }

        LocationActivity.openActivity(context, latitude, longitude, label);

//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=20.5666,45.345"));
//        context.startActivity(intent);
    }

    public static void openWebPage(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static boolean validatePass(String pass) {
        return pass.length() > 5;
    }

    public static boolean validatePhone(String phone) {
        return phone.length() > 7 && phone.length() < 15;
    }

    public static boolean validateEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getNationalityFileFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("Nationality.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return json;
    }
    public static String getFitnessLevelFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("FitnessLevel.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return json;
    }
    public static String getTimeFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("FilterTimeModel.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return json;
    }
    public static String getPriceModelFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("PriceModel.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return json;
    }


    public static boolean isSpecialClass(ClassModel model) {
        return model!=null && model.getPriceModel()!=null
                && ((model.getPriceModel().equals("SPECIAL")
                || model.getPriceModel().equalsIgnoreCase("PT")
                || model.getPriceModel().equalsIgnoreCase("WORKSHOP")
                || model.getPriceModel().equals("FOC")));
    }

    public static boolean isFreeClass(ClassModel model) {
        return model!=null&&model.getPriceModel()!=null&&model.getPriceModel().equals("FOC");
    }

    public static boolean isFemalesAllowed(ClassModel classModel) {
        return classModel.getClassType().equals(AppConstants.ApiParamValue.GENDER_BOTH) || classModel.getClassType().equals(AppConstants.ApiParamValue.GENDER_FEMALE);
    }

    public static boolean isMalesAllowed(ClassModel classModel) {
        return classModel.getClassType().equals(AppConstants.ApiParamValue.GENDER_BOTH) || classModel.getClassType().equals(AppConstants.ApiParamValue.GENDER_MALE);
    }

    public static String getClassGenderText(String classType) {
        if (classType.equals(AppConstants.ApiParamValue.GENDER_MALE)) {
            return MyApp.context.getString(R.string.male_small);
        } else if (classType.equals(AppConstants.ApiParamValue.GENDER_FEMALE)) {
            return MyApp.context.getString(R.string.female_small);
        } else {
            return MyApp.context.getString(R.string.both);
        }
    }

    public static String getClassGenderTextForTracker(String classType) {
        if (classType.equals(AppConstants.ApiParamValue.GENDER_MALE)) {
            return "Male".toUpperCase();
        } else if (classType.equals(AppConstants.ApiParamValue.GENDER_FEMALE)) {
            return "Female".toUpperCase();
        } else {
            return "Both".toUpperCase();
        }
    }

    public static void setClassJoinEventData(ClassModel classModel, ClassModel joinedData) {
        classModel.setUserJoinStatus(joinedData.isUserJoinStatus());
        classModel.setAvailableSeat(joinedData.getAvailableSeat());
        classModel.setJoinClassId(joinedData.getJoinClassId());
        if(joinedData.getRefBookingId()!=null)
        classModel.setRefBookingId(joinedData.getRefBookingId());
        else
            classModel.setRefBookingId(null);

    }
    public static void setWaitlistRemoveData(ClassModel classModel, ClassModel joinedData) {
        classModel.setWishType(joinedData.getWishType());
    }
    public static void setWaitlistAddData(ClassModel classModel, ClassModel joinedData) {
        classModel.setWishType(joinedData.getWishType());
    }

    public static void setPackageImage(ImageView imageView, int packageId) {
        switch (packageId) {
            case 1:
                imageView.setImageResource(R.drawable.ready_icon);
                break;
            case 4:
                imageView.setImageResource(R.drawable.star);
                break;
            case 2:
                imageView.setImageResource(R.drawable.set);
                break;
            case 3:
                imageView.setImageResource(R.drawable.pro_icon);
                break;
            case AppConstants.Values.SPECIAL_CLASS_ID:
                imageView.setImageResource(R.drawable.set_icon);
                break;
            default:
                imageView.setImageResource(R.drawable.ready_icon);
                break;
        }
    }

    public static String getSpecialClassText(ClassModel model) {
        String text = "Special Class";
        if (!model.getSpecialClassRemark().isEmpty()) {
            text = model.getSpecialClassRemark();
        }
        return text.trim();
    }

    public static void handleLogin(Context context) {
        if (TempStorage.getUser() == null) {
            InfoScreen.open(context);
        } else {
            ContinueUser.open(context);
        }
    }

    public static void shareGym(Context context, int id, String name) {
        CharSequence gymShareMessage = String.format(context.getString(R.string.share_message_gym), name + "");

        String url = getUrlBase() + "/share/gym/" + id + "/" + name;
        shareUrl(context, gymShareMessage + url.replace(" ", ""));
    }

    public static void shareTrainer(Context context, int id, String name) {
        CharSequence trainerShareMessage = String.format(context.getString(R.string.share_message_trainer), name + "");

        String url = getUrlBase() + "/share/trainer/" + id + "/" + name;
        shareUrl(context, trainerShareMessage + url.replace(" ", ""));
    }

    public static void shareClass(Context context, int id, String name) {
        CharSequence classShareMessage = String.format(context.getString(R.string.share_message), name + "");
        String url = getUrlBase() + "/share/classes/" + id + "/" + name;
        shareUrl(context, classShareMessage + url.replace(" ", ""));
    }
    public static String classEventDescription(Context context, int id, String name) {
        CharSequence classShareMessage = String.format(context.getString(R.string.class_event_description),name+"");
        String url = getUrlBase() + "/share/classes/" + id + "/" + name;
        return classShareMessage+url.replace(" ", "");
    }
    private static String getUrlBase() {
        String urlBase = BuildConfig.BASE_URL_SHARE;

        return urlBase;
    }

    private static void shareUrl(Context context, String url) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Share with"));
    }

    public static void wishlistItemRemoved(ClassModel classModel, ClassModel data) {

    }

    public static List<String> getCategoryListStringFromList(List<ClassActivity> list) {
        List<String> nameList = new ArrayList<>();

        if (list != null && !list.isEmpty()) {

            try {
                for (int index = 0; index < list.size(); index++) {
                    String value = list.get(index).getName();
                    nameList.add(value);

                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
        }



        return nameList;
    }

    public static void turnScreenOnThroughKeyguard(@NonNull Activity activity) {
        userPowerManagerWakeup(activity);
        useWindowFlags(activity);
        useActivityScreenMethods(activity);
    }

    private static void useActivityScreenMethods(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            try {
                activity.setTurnScreenOn(true);
                activity.setShowWhenLocked(true);
            } catch (NoSuchMethodError e) {
                Log.e(e.getMessage(), "Enable setTurnScreenOn and setShowWhenLocked is not present on device!");
            }
        }
    }

    private static void useWindowFlags(@NonNull Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private static void userPowerManagerWakeup(@NonNull Activity activity) {
        PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "p5m:class");
        wakelock.acquire(TimeUnit.MINUTES.toMillis(30));
    }

    public static boolean isTrainerOrGym(ClassModel classModel, VideoStatusData user) {
        if(classModel.getGymBranchDetail().getGymId()==user.mUid||classModel.getTrainerDetail()!=null&&classModel.getTrainerDetail().getId()==user.mUid){
            return true;
        }
            return false;
    }
    public static String getbase64EncodedString(String string){
        return Base64.encodeToString(string.getBytes(),Base64.NO_WRAP);
    }
    public static String getNetworkQualityTx(int quality){
        switch (quality){
            case 0:
                return "UNKNOWN";
            case 1:
                return "EXCELLENT";
            case 2:
                return "GOOD";
            case 3:
                return "POOR";
            case 4:
                return "BAD";
            case 5:
                return "VBAD";
            case 6:
                return "DOWN";
            case 8:
                return "DETECTING";
            default:
                return "";
        }
    }
    public static boolean checkIfClassIsFirstOrThird(ClassModel model){
        boolean checkIfFirstOrThird=false;
        List<ClassModel> mList= TempStorage.getAttendedClasses();
        if(model!=null&&mList!=null&&mList.size()>0){
            for(int i=0;i<mList.size();i++){
                ClassModel session = mList.get(i);
                if(model.getClassSessionId()==session.getClassSessionId()){
                    if(i==0||i==2){
                        checkIfFirstOrThird = true;
                        break;
                    }
                }
            }
        }
        return checkIfFirstOrThird;
    }

}
