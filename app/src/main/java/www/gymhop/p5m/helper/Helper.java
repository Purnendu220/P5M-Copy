package www.gymhop.p5m.helper;

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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import www.gymhop.p5m.R;
import www.gymhop.p5m.data.main.ClassActivity;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.GymBranchDetail;
import www.gymhop.p5m.data.main.GymDetailModel;
import www.gymhop.p5m.data.main.MediaModel;
import www.gymhop.p5m.data.main.TrainerDetailModel;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.KeyboardUtils;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.custom.GalleryActivity;

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
            buttonJoin.setText(context.getString(R.string.booked));
            buttonJoin.setBackgroundResource(R.drawable.joined_rect);
        } else if (model.getAvailableSeat() == 0) {
            buttonJoin.setText(context.getString(R.string.full));
            buttonJoin.setBackgroundResource(R.drawable.full_rect);
        } else {
            buttonJoin.setText(context.getString(R.string.book));
            buttonJoin.setBackgroundResource(R.drawable.join_rect);
        }
    }

    public static void setJoinStatusProfile(Context context, TextView view, ClassModel model) {
        if (model.isUserJoinStatus()) {
            view.setText(context.getString(R.string.booked));
            view.setBackgroundResource(R.drawable.theme_bottom_text_button_booked);
            view.setEnabled(false);
        } else if (model.getAvailableSeat() == 0) {
            view.setText(context.getString(R.string.full));
            view.setBackgroundResource(R.drawable.theme_bottom_text_button_full);
            view.setEnabled(false);
        } else {
            view.setText(context.getString(R.string.reserve_class));
            view.setBackgroundResource(R.drawable.theme_bottom_text_button_book);
            view.setEnabled(true);
        }
    }

    public static void setFavButtonTemp(Context context, Button buttonFav, boolean isFollow) {
        setFavButton(context, buttonFav, isFollow);
    }

    public static void setFavButton(Context context, Button buttonFav, TrainerModel model) {
        setFavButton(context, buttonFav, model.isIsfollow());
    }

    public static void setFavButton(Context context, Button buttonFav, TrainerDetailModel model) {
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

    public static void openImageListViewer(Context context, List<MediaModel> list, int position) {

        List<String> images = new ArrayList<>(list.size());
        for (MediaModel mediaModel : list) {
            images.add(mediaModel.getMediaUrl());
        }

        GalleryActivity.openActivity(context, null, null, position, images);


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

    public static void openImageViewer(Context context, Activity activity, View sharedElement, String url) {

        List<String> images = new ArrayList<>(1);
        images.add(url);

        GalleryActivity.openActivity(context, activity, sharedElement, 0, images);

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

    public static void openMap(Context context, Double latitude, Double longitude) {
        if (longitude == null || latitude == null) {
            return;
        }

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=20.5666,45.345"));
        context.startActivity(intent);
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
            return "Male";
        } else if (classType.equals(AppConstants.ApiParamValue.GENDER_FEMALE)) {
            return "Female";
        } else {
            return "Both";
        }
    }

    public static void setClassJoinEventData(ClassModel classModel, ClassModel joinedData) {
        classModel.setUserJoinStatus(joinedData.isUserJoinStatus());
        classModel.setAvailableSeat(joinedData.getAvailableSeat());
    }

    public static void setPackageImage(ImageView imageView, String packageName) {
        switch (packageName.toLowerCase()) {
            case "ready":
                imageView.setImageResource(R.drawable.ready_icon);
                break;
            case "visit":
                imageView.setImageResource(R.drawable.star);
                break;
            case "get set":
                imageView.setImageResource(R.drawable.set);
                break;
            case "pro":
                imageView.setImageResource(R.drawable.pro_icon);
                break;
            case "special":
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
}
