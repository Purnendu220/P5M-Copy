package com.p5m.me.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.RefrenceWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookForAFriendPopup extends Dialog implements View.OnClickListener {


    private final int navigationFrom;
    private Context mContext;



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





    public BookForAFriendPopup(@NonNull Context context, boolean isThereOtherClassToRate, int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext=context;
        this.navigationFrom=navigatinFrom;
        init(context);
    }
    private void init(Context context) {
        setContentView(R.layout.view_bookforfriend);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();


    }

    private void setListeners(){
        textViewBookWithFriend.setOnClickListener(this);



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

                dismiss();
            }
            break;
            case R.id.textViewIWillDoLater:{
                dismiss();

            }
            break;


        }

    }


}
