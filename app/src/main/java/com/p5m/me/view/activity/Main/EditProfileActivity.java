package com.p5m.me.view.activity.Main;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.p5m.me.R;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.MediaResponse;
import com.p5m.me.data.Nationality;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.UserInfoUpdate;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.firebase_dynamic_link.FirebaseDynamicLinnk;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.KeyboardUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.robertlevonyan.components.picker.ItemModel;
import com.robertlevonyan.components.picker.OnPickerCloseListener;
import com.robertlevonyan.components.picker.PickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener, NetworkCommunicator.RequestListener {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, EditProfileActivity.class));
    }
    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        return intent;
    }


    @BindView(R.id.editTextNameFirst)
    public EditText editTextNameFirst;
    @BindView(R.id.editTextNameLast)
    public EditText editTextNameLast;
    @BindView(R.id.editTextEmail)
    public EditText editTextEmail;
    @BindView(R.id.editTextMobile)
    public EditText editTextMobile;

    @BindView(R.id.buttonMale)
    public Button buttonMale;
    @BindView(R.id.buttonFemale)
    public Button buttonFemale;

    @BindView(R.id.layoutChooseFocus)
    public View layoutChooseFocus;
    @BindView(R.id.layoutChangePass)
    public View layoutChangePass;
    @BindView(R.id.layoutDob)
    public View layoutDob;

    @BindView(R.id.textViewLocation)
    public TextView textViewLocation;
    @BindView(R.id.textViewNationality)
    public TextView textViewNationality;
    @BindView(R.id.textViewDob)
    public TextView textViewDob;

    @BindView(R.id.imageViewProfile)
    public ImageView imageViewProfile;
    @BindView(R.id.imageViewDob)
    public ImageView imageViewDob;
    @BindView(R.id.imageViewBack)
    public ImageView imageViewBack;
    @BindView(R.id.imageViewCamera)
    public ImageView imageViewCamera;

    @BindView(R.id.progressBar)
    public View progressBar;

    @BindView(R.id.imageViewDone)
    public ImageView imageViewDone;
    @BindView(R.id.progressBarDone)
    public ProgressBar progressBarDone;

    public Date date;
    public String gender;
    public Handler handler;
    private boolean haveNationality;
    private boolean haveLocation;
    private UserInfoUpdate userInfoUpdate;

    public Runnable runnableEmailValidation, runnablePhoneValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ButterKnife.bind(activity);

        runnableEmailValidation = new Runnable() {
            @Override
            public void run() {
                if (editTextEmail.getText().toString().isEmpty() || Helper.validateEmail(editTextEmail.getText().toString())) {
                    editTextEmail.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
                    editTextEmail.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                } else {
                    editTextEmail.setTextColor(ContextCompat.getColor(context, R.color.theme_error_text));
                    editTextEmail.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                }
            }
        };

        runnablePhoneValidation = new Runnable() {
            @Override
            public void run() {
                if (editTextMobile.getText().toString().isEmpty() || Helper.validatePhone(editTextMobile.getText().toString())) {
                    editTextMobile.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
                    editTextMobile.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
                } else {
                    editTextMobile.setTextColor(ContextCompat.getColor(context, R.color.theme_error_text));
                    editTextMobile.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
                }
            }
        };

        handler = new Handler(Looper.getMainLooper());

        layoutChooseFocus.setOnClickListener(this);
        layoutChangePass.setOnClickListener(this);
        layoutDob.setOnClickListener(this);
        textViewLocation.setOnClickListener(this);
        textViewNationality.setOnClickListener(this);
        imageViewDob.setOnClickListener(this);
//        buttonMale.setOnClickListener(this);
//        buttonFemale.setOnClickListener(this);
        imageViewCamera.setOnClickListener(this);

        setUser();
        FirebaseDynamicLinnk.getDynamicLink(this,getIntent());
        editTextMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler.removeCallbacks(runnablePhoneValidation);
                handler.postDelayed(runnablePhoneValidation, 300);
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler.removeCallbacks(runnableEmailValidation);
                handler.postDelayed(runnableEmailValidation, 300);
            }
        });
    }



    private void setUser() {
        Long dob = TempStorage.getUser().getDob();
        if (dob != null) {
            textViewDob.setVisibility(View.VISIBLE);
            imageViewDob.setVisibility(View.GONE);
            textViewDob.setText(DateUtils.getFormattedDobFromDisplay(new Date(dob)));
        } else {
            textViewDob.setVisibility(View.GONE);
            imageViewDob.setVisibility(View.VISIBLE);
        }

        ImageUtils.setImage(context, TempStorage.getUser().getProfileImage(), imageViewProfile);

        setNationality(TempStorage.getUser().getNationality());
        setLocation(TempStorage.getUser().getLocation());

        if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)) {
            selectMale();
        } else if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)) {
            selectFemale();
        }

        editTextMobile.setText(TempStorage.getUser().getMobile());
        editTextEmail.setText(TempStorage.getUser().getEmail());

        editTextNameFirst.setText(TempStorage.getUser().getFirstName());
        editTextNameLast.setText(TempStorage.getUser().getLastName());

    }

    private void selectFemale() {
        buttonMale.setBackgroundResource(R.color.white);
        buttonMale.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        buttonFemale.setBackgroundResource(R.color.colorAccent);
        buttonFemale.setTextColor(ContextCompat.getColor(context, R.color.white));
        gender = AppConstants.ApiParamValue.GENDER_FEMALE;
    }

    private void selectMale() {
        buttonMale.setBackgroundResource(R.color.colorAccent);
        buttonMale.setTextColor(ContextCompat.getColor(context, R.color.white));
        buttonFemale.setBackgroundResource(R.color.white);
        buttonFemale.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        gender = AppConstants.ApiParamValue.GENDER_MALE;
    }

    @OnClick(R.id.imageViewDone)
    public void imageViewDone(View view) {

        String email = editTextEmail.getText().toString().trim();
        String phone = editTextMobile.getText().toString().trim();

        boolean isEmailValid = true;
        boolean isPhoneValid = true;

        if (editTextNameFirst.getText().toString().trim().isEmpty()) {
            ToastUtils.show(context, R.string.enter_first_name);
            editTextNameFirst.requestFocus();
            return;
        }
        if (editTextNameLast.getText().toString().trim().isEmpty()) {
            ToastUtils.show(context, R.string.enter_last_name);
            editTextNameLast.requestFocus();
            return;
        }

        if (editTextEmail.getText().toString().trim().isEmpty()) {
            ToastUtils.show(context, getString(R.string.please_enter_your_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!email.isEmpty() && !Helper.validateEmail(email)) {
            editTextEmail.setTextColor(ContextCompat.getColor(context, R.color.theme_error_text));
            editTextEmail.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            isEmailValid = false;
        }

        if (!phone.isEmpty() && !Helper.validatePhone(phone)) {
            editTextMobile.setTextColor(ContextCompat.getColor(context, R.color.theme_error_text));
            editTextMobile.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            isPhoneValid = false;
        }

        if (!isEmailValid && !isPhoneValid) {
            ToastUtils.showLong(context, context.getResources().getString(R.string.phone_email_required_validate));
        } else if (!isEmailValid && isPhoneValid) {
            ToastUtils.showLong(context, context.getResources().getString(R.string.email_required_validate));
        } else if (isEmailValid && !isPhoneValid) {
            ToastUtils.showLong(context, context.getResources().getString(R.string.phone_required_validate));
        }

        if (!isEmailValid || !isPhoneValid) {
            return;
        }

        imageViewDone.setVisibility(View.GONE);
        progressBarDone.setVisibility(View.VISIBLE);

        userInfoUpdate = new UserInfoUpdate(TempStorage.getUser().getId());

        if (gender != null) {
            userInfoUpdate.setGender(gender);
        }

        if (date != null) {
            userInfoUpdate.setDob(DateUtils.getFormattedDobFromServer(date));
        }

        userInfoUpdate.setEmail(email);
        userInfoUpdate.setMobile(phone);

        if (haveLocation) {
            userInfoUpdate.setLocation(textViewLocation.getText().toString().trim());
        }

        if (haveNationality) {
            userInfoUpdate.setNationality(textViewNationality.getText().toString().trim());
        }

        userInfoUpdate.setFirstName(editTextNameFirst.getText().toString().trim());
       userInfoUpdate.setLastName(editTextNameLast.getText().toString().trim());
       // userInfoUpdate.setFirstName(editTextNameFirst.getText().toString().trim() + " " + editTextNameLast.getText().toString().trim());


        if (!TempStorage.getUser().getEmail().equals(email)) {
            networkCommunicator.validateEmail(email, this, false);
        } else {
            networkCommunicator.userInfoUpdate(TempStorage.getUser().getId(), userInfoUpdate, this, false);
        }
    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewCamera:
                imagePicker();
                break;
            case R.id.buttonMale:
                selectMale();
                break;
            case R.id.buttonFemale:
                selectFemale();
                break;
            case R.id.layoutChooseFocus:
                ChooseFocusActivity.openActivity(context);
                break;
            case R.id.layoutDob:
                dialogDob();
                break;
            case R.id.layoutChangePass:
                if (TempStorage.getUser().getFacebookId()==null||TempStorage.getUser().getFacebookId().isEmpty()||TempStorage.getUser().getFacebookId().equalsIgnoreCase("0")) {
                    ChangePasswordActivity.openActivity(context);
                } else {
                    dialogFBChangePass();
                }
                break;
            case R.id.textViewNationality:
                ChooseNationalityActivity.openActivity(activity);
                break;
            case R.id.textViewLocation:

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(activity), AppConstants.ResultCode.CHOOSE_LOCATION);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    LogUtils.exception(e);
                    ToastUtils.show(context, "Please update your google play services");
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    LogUtils.exception(e);
                    ToastUtils.show(context, "Google play services is not available");
                }
                break;
        }
    }

    private void dialogFBChangePass() {

        final MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .cancelable(true)
                .customView(R.layout.dialog_fb_user_change_pass, false)
                .build();
        materialDialog.show();

        final TextView textViewOk = (TextView) materialDialog.findViewById(R.id.textViewOk);
        final TextView textViewClose = (TextView) materialDialog.findViewById(R.id.textViewCancel);

        textViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
                ChangePasswordActivity.openActivity(context);
            }
        });

        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (TempStorage.getUser().getEmail().isEmpty()) {
//                    ToastUtils.show(context, getString(R.string.mention_email));
//                    materialDialog.dismiss();
//                    editTextEmail.requestFocus();
//                    return;
//                }

                textViewOk.setVisibility(View.GONE);

                networkCommunicator.forgotPassword(TempStorage.getUser().getEmail(), new NetworkCommunicator.RequestListener() {
                    @Override
                    public void onApiSuccess(Object response, int requestCode) {
                        String message = ((ResponseModel<String>) response).data;

                        DialogUtils.showBasicMessage(context, message, context.getResources().getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        });

                        textViewOk.setVisibility(View.VISIBLE);
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onApiFailure(String errorMessage, int requestCode) {
                        textViewOk.setVisibility(View.VISIBLE);

                        ToastUtils.showLong(context, errorMessage);
                        materialDialog.dismiss();
                    }
                }, false);
            }
        });
    }

    private void dialogDob() {
        Calendar calendar = Calendar.getInstance();

        int yearBirth = calendar.get(Calendar.YEAR);
        int monthBirth = calendar.get(Calendar.MONTH);
        int dayBirth = calendar.get(Calendar.DAY_OF_MONTH);

        if (TempStorage.getUser().getDob() != null) {
            calendar.setTime(new Date(TempStorage.getUser().getDob()));

            yearBirth = calendar.get(Calendar.YEAR);
            monthBirth = calendar.get(Calendar.MONTH);
            dayBirth = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog dialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                date = c.getTime();

                textViewDob.setText(DateUtils.getFormattedDobFromDisplay(date));

                textViewDob.setVisibility(View.VISIBLE);
                imageViewDob.setVisibility(View.GONE);
            }
        }, yearBirth, monthBirth, dayBirth);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.ResultCode.CHOOSE_NATIONALITY && resultCode == RESULT_OK) {

            Nationality nationality = (Nationality) data.getSerializableExtra(AppConstants.DataKey.NATIONALITY_OBJECT);

            if (nationality != null) {
                setNationality(nationality.getNationality());
            }

        } else if (requestCode == AppConstants.ResultCode.CHOOSE_LOCATION && resultCode == RESULT_OK) {

            Place place = PlacePicker.getPlace(context, data);

            if (place != null) {
                setLocation(place.getAddress().toString());
            }
        }
    }

    private void setNationality(String nationality) {
        if (!nationality.isEmpty()) {
            haveNationality = true;
            textViewNationality.setText(nationality);
            textViewNationality.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        } else {
            textViewNationality.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
            textViewNationality.setText(R.string.nationality);
        }
    }

    private void setLocation(String location) {
        if (!location.isEmpty()) {
            haveLocation = true;
            textViewLocation.setText(location);
            textViewLocation.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        } else {
            textViewLocation.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
            textViewLocation.setText(R.string.location);
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        imageViewDone.setVisibility(View.VISIBLE);
        progressBarDone.setVisibility(View.GONE);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UPDATE_USER: {

                ToastUtils.show(context, R.string.profile_changes_saved);
                User user = ((ResponseModel<User>) response).data;

                User userOld = TempStorage.getUser();

                if (!user.getFirstName().trim().equals(userOld.getFirstName().trim())) {
                    MixPanel.trackEditProfile(AppConstants.Tracker.FIRST_NAME_CHANGED);
                }
                if (!user.getLastName().trim().equals(userOld.getLastName().trim())) {
                    MixPanel.trackEditProfile(AppConstants.Tracker.LAST_NAME_CHANGED);
                }
                if (!user.getEmail().equals(userOld.getEmail())) {
                    MixPanel.trackEditProfile(AppConstants.Tracker.EMAIL_CHANGED);
                }

                if (!user.getMobile().equals(userOld.getMobile())) {
                    MixPanel.trackEditProfile(AppConstants.Tracker.MOBILE_NUMBER_CHANGED);
                }

                if (!user.getGender().equals(userOld.getGender())) {
                    MixPanel.trackEditProfile(AppConstants.Tracker.GENDER_CHANGED);
                }

                if (!user.getLocation().equals(userOld.getLocation())) {
                    MixPanel.trackEditProfile(AppConstants.Tracker.LOCATION_CHANGED);
                }

                if (!user.getNationality().equals(userOld.getNationality())) {
                    MixPanel.trackEditProfile(AppConstants.Tracker.NATIONALITY_CHANGED);
                }

                if (!String.valueOf(user.getDob()).equals(String.valueOf(userOld.getDob()))) {

                    MixPanel.trackEditProfile(AppConstants.Tracker.DOB_CHANGED);
                }

                EventBroadcastHelper.sendUserUpdate(context, user);
                finish();
            }
            break;
            case NetworkCommunicator.RequestCode.PHOTO_UPLOAD:

                MediaResponse mediaResponse = ((ResponseModel<MediaResponse>) response).data;

                MixPanel.trackEditProfile(AppConstants.Tracker.PROFILE_IMAGE_CHANGED);

                if (activity != null && !activity.isFinishing()) {
                    ImageUtils.setImageDelay(context, mediaResponse.getMediaPath(), imageViewProfile);
                }

                User user = TempStorage.getUser();
                user.setUserProfileImageId(mediaResponse.getId());
                user.setProfileImage(mediaResponse.getMediaPath());
                user.setProfileImageThumbnail(mediaResponse.getThumbnailPath());

                EventBroadcastHelper.sendUserUpdate(context, user);

                if (activity != null && !activity.isFinishing()) {
                    progressBar.setVisibility(View.GONE);
                    imageViewCamera.setVisibility(View.VISIBLE);
                }

                break;
            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:
                networkCommunicator.userInfoUpdate(TempStorage.getUser().getId(), userInfoUpdate, this, false);

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        imageViewDone.setVisibility(View.VISIBLE);
        progressBarDone.setVisibility(View.GONE);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UPDATE_USER:
                ToastUtils.showLong(context, errorMessage);

                break;
            case NetworkCommunicator.RequestCode.PHOTO_UPLOAD:

                ToastUtils.showLong(context, errorMessage);

                if (activity != null && !activity.isFinishing()) {
                    progressBar.setVisibility(View.GONE);
                    imageViewCamera.setVisibility(View.VISIBLE);

                    ImageUtils.clearImage(context, imageViewProfile);
                    imageViewProfile.setImageResource(0);
                }
                break;
            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:

                editTextEmail.requestFocus();
                KeyboardUtils.open(editTextEmail, context);
                ToastUtils.show(context, errorMessage);

                break;

        }
    }

    private PickerDialog pickerDialog;

    private void imagePicker() {
//        ImagePicker.setMinQuality(1280, 1280);
//        ImagePicker.pickImage(activity, "Pick your image:");

        ArrayList<ItemModel> itemModels = new ArrayList<>();
        itemModels.add(new ItemModel(ItemModel.ITEM_CAMERA, "", 0, false, 0, 0));
        itemModels.add(new ItemModel(ItemModel.ITEM_GALLERY, "", 0, false, 0, 0));
//        itemModels.add(new ItemModel(ItemModel.ITEM_FILES, "", 0, false, 0, 0));

        pickerDialog = new PickerDialog.Builder((BaseActivity) activity)// Activity or Fragment
                .setTitle(context.getResources().getString(R.string.pick_image))          // String value or resource ID
                .setTitleTextColor(ContextCompat.getColor(context, R.color.theme_dark_text)) // Color of title text
                .setListType(PickerDialog.TYPE_LIST, 3)       // Type of the picker, must be PickerDialog.TYPE_LIST or PickerDialog.TYPE_Grid
                .setItems(itemModels)
                .create();

        pickerDialog.setOnPickerCloseListener(new OnPickerCloseListener() {
            @Override
            public void onPickerClosed(long type, Uri uri) {

                String path = ImageUtils.getImagePathFromUri(context, uri);

                if (path != null && !path.isEmpty()) {

                    progressBar.setVisibility(View.VISIBLE);
                    imageViewCamera.setVisibility(View.GONE);
                    imageViewProfile.setImageURI(uri);
                    networkCommunicator.uploadUserImage(context, new File(path), EditProfileActivity.this, false);
                }

//                if (type == ItemModel.ITEM_CAMERA) {
//                    imageViewProfile.setImageURI(uri);
//                } else if (type == ItemModel.ITEM_GALLERY) {
//                    imageViewProfile.setImageURI(uri);
//                } else if (type == ItemModel.ITEM_FILES) {
//                    imageViewProfile.setImageURI(uri);
//                }
            }
        });

        pickerDialog.show();

//        uploadUserImage
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                pickerDialog.dismiss();
                if (Manifest.permission.CAMERA.equals(permission)) {
                    showPermissionImportantAlert(context.getResources().getString(R.string.permission_message_camera));
                    return;
                    }
                else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                    showPermissionImportantAlert(context.getResources().getString(R.string.permission_message_media));
                    return;
                    }
                else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                    showPermissionImportantAlert(context.getResources().getString(R.string.permission_message_media));

                    return;
                    }
            }
        }
            try{
                pickerDialog.onPermissionsResult(requestCode, permissions, grantResults);

            }catch (Exception e){
                e.printStackTrace();
                pickerDialog.dismiss();
            }



    }
    private void showPermissionImportantAlert(String message){
        DialogUtils.showBasicMessage(context,context.getResources().getString(R.string.permission_alert), message,
                context.getResources().getString(R.string.go_to_settings), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + context.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        context.startActivity(i);
                        dialog.dismiss();
                        }
                },context.getResources().getString(R.string.cancel), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
    }


//    @Override
//    public void onBackPressed() {
//        DialogUtils.showBasic(context, "Are you sure want to exit?", "Exit", new MaterialDialog.SingleButtonCallback() {
//            @Override
//            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                EditProfileActivity.super.onBackPressed();
//            }
//        });
//    }

}
