package com.p5m.me.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.RefrenceWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookForAFriendPopup extends Dialog implements View.OnClickListener {


    private final int navigationFrom;
    private Context mContext;
    private ClassModel model;



    @BindView(R.id.textInputLayoutFriendsName)
    TextInputLayout textInputLayoutFriendsName;

    @BindView(R.id.editTextFriendsName)
    EditText editTextFriendsName;

    @BindView(R.id.textInputLayoutFriendsEmail)
    TextInputLayout textInputLayoutFriendsEmail;

    @BindView(R.id.editTextFriendsEmail)
    EditText editTextFriendsEmail;

    @BindView(R.id.textViewBookWithFriend)
    TextView textViewBookWithFriend;

    @BindView(R.id.buttonMale)
    public Button buttonMale;

    @BindView(R.id.buttonFemale)
    public Button buttonFemale;

    @BindView(R.id.textViewGenderError)
    public TextView textViewGenderError;

    private String gender;






    public BookForAFriendPopup(@NonNull Context context, ClassModel model, int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext=context;
        this.navigationFrom=navigatinFrom;
        this.model=model;
        init(context);
    }
    private void init(Context context) {
        setContentView(R.layout.view_bookforfriend);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();
        selectGender(model.getClassType());


    }

    private void setListeners(){
        textViewBookWithFriend.setOnClickListener(this);
        buttonFemale.setOnClickListener(this);
        buttonMale.setOnClickListener(this);
        }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textViewBookWithFriend:{
                String name = editTextFriendsName.getText().toString().trim();
                String email = editTextFriendsEmail.getText().toString().trim();

                if (name.isEmpty()) {
                    textInputLayoutFriendsName.setError(mContext.getResources().getString(R.string.name_required_error));
                    return;
                }
                if (email.isEmpty()) {
                    textInputLayoutFriendsEmail.setError(mContext.getResources().getString(R.string.email_error_text));
                    return;
                }
                if (!Helper.validateEmail(email)) {
                    textInputLayoutFriendsEmail.setError(mContext.getResources().getString(R.string.email_error_text));
                    return;
                }
                if (gender == null) {
                    textViewGenderError.setVisibility(View.VISIBLE);
                    textViewGenderError.setText(mContext.getResources().getText(R.string.gender_required));
                    return;
                }

                EventBroadcastHelper.sendBookWithFriendEvent(new BookWithFriendData(name,email,gender));
                dismiss();

            }
            break;
            case R.id.textViewIWillDoLater:{
                dismiss();

            }
            break;
            case R.id.buttonMale:
                selectGender(AppConstants.ApiParamValue.GENDER_MALE);

                break;

            case R.id.buttonFemale:
                selectGender(AppConstants.ApiParamValue.GENDER_FEMALE);
                break;


        }

    }

    private void selectGender(String classCategory){
        if(classCategory.equalsIgnoreCase(AppConstants.ApiParamValue.GENDER_FEMALE)){
            textViewGenderError.setVisibility(View.INVISIBLE);

            buttonMale.setBackgroundResource(R.drawable.button_white);
            buttonFemale.setBackgroundResource(R.drawable.join_rect);
            buttonMale.setTextColor(ContextCompat.getColor(mContext, R.color.theme_dark_text));
            buttonFemale.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            gender = AppConstants.ApiParamValue.GENDER_FEMALE;
        }
        if(classCategory.equalsIgnoreCase(AppConstants.ApiParamValue.GENDER_MALE)){
            textViewGenderError.setVisibility(View.INVISIBLE);

            buttonMale.setBackgroundResource(R.drawable.join_rect);
            buttonFemale.setBackgroundResource(R.drawable.button_white);
            buttonMale.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            buttonFemale.setTextColor(ContextCompat.getColor(mContext, R.color.theme_dark_text));

            gender = AppConstants.ApiParamValue.GENDER_MALE;
        }

    }


}
