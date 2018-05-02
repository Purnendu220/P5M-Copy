package www.gymhop.p5m.view.activity.Main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.Nationality;
import www.gymhop.p5m.data.main.User;
import www.gymhop.p5m.data.request.UserInfoUpdate;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DateUtils;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener, NetworkCommunicator.RequestListener {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, EditProfileActivity.class));
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
    @BindView(R.id.imageViewStatus)
    public ImageView imageViewStatus;

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
        buttonMale.setOnClickListener(this);
        buttonFemale.setOnClickListener(this);
        imageViewStatus.setOnClickListener(this);

        setUser();

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

        setNationality(TempStorage.getUser().getNationality());
        setLocation(TempStorage.getUser().getLocation());

        if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)) {
            selectMale();
        } else if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)) {
            selectFemale();
        }

        editTextMobile.setText(TempStorage.getUser().getMobile());
        editTextEmail.setText(TempStorage.getUser().getEmail());

        String[] names = TempStorage.getUser().getFirstName().split(" ");

        if (names.length > 1) {
            String name = TempStorage.getUser().getFirstName();
            try {
                editTextNameFirst.setText(names[0]);
                editTextNameLast.setText(name.substring(name.indexOf(" ") + 1, name.length()));
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
                editTextNameFirst.setText(TempStorage.getUser().getFirstName());
                editTextNameLast.setText("");
            }
        } else {
            editTextNameFirst.setText(TempStorage.getUser().getFirstName());
        }
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

        UserInfoUpdate userInfoUpdate = new UserInfoUpdate(TempStorage.getUser().getId());

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

//        userInfoUpdate.setFirstName(editTextNameFirst.getText().toString().trim());
//        userInfoUpdate.setLastName(editTextNameLast.getText().toString().trim());
        userInfoUpdate.setFirstName(editTextNameFirst.getText().toString().trim() + " " + editTextNameLast.getText().toString().trim());

        networkCommunicator.userInfoUpdate(TempStorage.getUser().getId(), userInfoUpdate, this, false);
    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewStatus:
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
                ChangePasswordActivity.openActivity(context);
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
        } else if (requestCode == AppConstants.ResultCode.CHOOSE_LOCATION && resultCode == RESULT_OK) {

//            Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
//            if (bitmap != null) {
//                imageViewProfile.setImageBitmap(bitmap);
//            }
        }


    }

    private void setNationality(String nationality) {
        if (!nationality.isEmpty()) {
            haveNationality = true;
            textViewNationality.setText(nationality);
            textViewNationality.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        } else {
            textViewNationality.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
            textViewNationality.setText("Nationality");
        }
    }

    private void setLocation(String location) {
        if (!location.isEmpty()) {
            haveLocation = true;
            textViewLocation.setText(location);
            textViewLocation.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        } else {
            textViewLocation.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
            textViewLocation.setText("Location");
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        imageViewDone.setVisibility(View.VISIBLE);
        progressBarDone.setVisibility(View.GONE);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UPDATE_USER:

                User user = ((ResponseModel<User>) response).data;
                EventBroadcastHelper.sendUserUpdate(context, user);
                finish();

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
        }
    }

    private void imagePicker() {
//        ImagePicker.setMinQuality(1280, 1280);
//        ImagePicker.pickImage(activity, "Pick your image:");

//        PickerDialog pickerDialog = PickerDialog.Builder(actitvty)// Activity or Fragment
//                .setTitle("Picke Image")          // String value or resource ID
//                       .setTitleTextSize(...)  // Text size of title
//                       .setTitleTextColor(...) // Color of title text
//                       .setListType(...)       // Type of the picker, must be PickerDialog.TYPE_LIST or PickerDialog.TYPE_Grid
//                       .setItems(...)          // List of ItemModel-s which should be in picker
//                       .create()
    }
}
