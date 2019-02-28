package com.p5m.me.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.p5m.me.BuildConfig;
import com.p5m.me.MyApp;
import com.p5m.me.R;
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
import com.p5m.me.view.activity.Main.ClassProfileActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.Main.LocationActivity;
import com.p5m.me.view.activity.Main.SearchActivity;
import com.p5m.me.view.custom.GalleryActivity;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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
            new RemoteConfigSetUp().setConfig(ClassProfileActivity.activityRef,buttonJoin,
                    RemoteConfigConst.BOOKED_BUTTON,context.getString(R.string.booked),RemoteConfigConst.ConfigStatus.TEXT);
            buttonJoin.setText(context.getString(R.string.booked));
            buttonJoin.setBackgroundResource(R.drawable.joined_rect);
        } else if (model.getAvailableSeat() == 0) {
            buttonJoin.setText(context.getString(R.string.full));
            buttonJoin.setBackgroundResource(R.drawable.full_rect);
        } else {
            new RemoteConfigSetUp().setConfig(ClassProfileActivity.activityRef,buttonJoin,
                    RemoteConfigConst.BOOK_BUTTON,context.getString(R.string.book),RemoteConfigConst.ConfigStatus.TEXT);

            buttonJoin.setText(context.getString(R.string.book));
            buttonJoin.setBackgroundResource(R.drawable.join_rect);
        }
    }

    public static void setJoinStatusProfile(Context context, TextView view,TextView view1, ClassModel model) {
        if(model.isExpired()){
            view.setText(context.getString(R.string.expired));
            view.setBackgroundResource(R.drawable.theme_bottom_text_button_full);
            view.setEnabled(false);
            view1.setVisibility(View.GONE);
        }
        else if (model.isUserJoinStatus()) {
            view.setText(context.getString(R.string.booked));
            view.setBackgroundResource(R.drawable.theme_bottom_text_button_booked);
            view.setEnabled(false);
            view1.setVisibility(View.GONE);

        } else if (model.getAvailableSeat() == 0) {
            view.setText(context.getString(R.string.full));
            view.setBackgroundResource(R.drawable.theme_bottom_text_button_full);
            view.setEnabled(false);
            view1.setVisibility(View.GONE);

        }
        else if(model.getAvailableSeat()<2){
            view1.setVisibility(View.GONE);

        }
        else {
            view.setText(context.getString(R.string.reserve_class));
            view.setBackgroundResource(R.drawable.theme_bottom_text_button_book);
            view.setEnabled(true);
            view1.setVisibility(View.VISIBLE);
            view1.setText(context.getString(R.string.reserve_class_with_friend));
            view1.setBackgroundResource(R.drawable.theme_bottom_text_button_book);
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
     public static void openImageListViewer(Context context, List<MediaModel> list, int position,String viewholderType) {

        List<String> images = new ArrayList<>(list.size());
        for (MediaModel mediaModel : list) {
            images.add(mediaModel.getMediaUrl());
        }

        GalleryActivity.openActivity(context, null, null, position, images,viewholderType);


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

    public static void openImageViewer(Context context, Activity activity, View sharedElement, String url,String viewholderType) {

        if (url == null || url.isEmpty()) {
            return;
        }

        List<String> images = new ArrayList<>(1);
        images.add(url);

        GalleryActivity.openActivity(context, activity, sharedElement, 0, images,viewholderType);

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
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
        return json;
    }

    public static boolean isSpecialClass(ClassModel model) {
        return model.getPriceModel().equals("SPECIAL") || model.getPriceModel().equals("FOC");
    }

    public static boolean isFreeClass(ClassModel model) {
        return model.getPriceModel().equals("FOC");
    }

    public static boolean isFemalesAllowed(ClassModel classModel) {
        return classModel.getClassType().equals(AppConstants.ApiParamValue.GENDER_BOTH) || classModel.getClassType().equals(AppConstants.ApiParamValue.GENDER_FEMALE);
    }

    public static boolean isMalesAllowed(ClassModel classModel) {
        return classModel.getClassType().equals(AppConstants.ApiParamValue.GENDER_BOTH) || classModel.getClassType().equals(AppConstants.ApiParamValue.GENDER_MALE);
    }

    public static String getClassGenderText(String classType) {
        if (classType.equals(AppConstants.ApiParamValue.GENDER_MALE)) {
            return MyApp.context.getString(R.string.male);
        } else if (classType.equals(AppConstants.ApiParamValue.GENDER_FEMALE)) {
            return MyApp.context.getString(R.string.female);
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
        CharSequence gymShareMessage = String.format(context.getString(R.string.share_message_gym),name+"");

        String url = getUrlBase() + "/share/gym/" + id + "/" + name;
        shareUrl(context, gymShareMessage+url.replace(" ", ""));
    }

    public static void shareTrainer(Context context, int id, String name) {
        CharSequence trainerShareMessage = String.format(context.getString(R.string.share_message_trainer),name+"");

        String url = getUrlBase() + "/share/trainer/" + id + "/" + name;
        shareUrl(context, trainerShareMessage+url.replace(" ", ""));
    }

    public static void shareClass(Context context, int id, String name) {
        CharSequence classShareMessage = String.format(context.getString(R.string.share_message),name+"");
        String url = getUrlBase() + "/share/classes/" + id + "/" + name;
        shareUrl(context, classShareMessage+url.replace(" ", ""));
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

}
