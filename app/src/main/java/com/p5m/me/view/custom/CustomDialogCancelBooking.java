package com.p5m.me.view.custom;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.p5m.me.R;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.view.activity.LoginRegister.InfoScreen;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CustomDialogCancelBooking extends Dialog implements OnClickListener {


    private final int navigatinFrom;
    private final OnCancelBooking listener;
    private final String message;
    private ClassModel classModel;
    private Context mContext;
    @BindView(R.id.textViewCancelBookingMessage)
    TextView textViewCancelBookingMessage;

    @BindView(R.id.textViewNoRefund)
    TextView textViewNoRefund;

    @BindView(R.id.textViewNoThanks)
    TextView textViewNoThanks;

    @BindView(R.id.textViewCancelBooking)
    TextView textViewCancelBooking;

    public CustomDialogCancelBooking(@NonNull Context context, OnCancelBooking listener, String message, ClassModel classModel,  int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.navigatinFrom = navigatinFrom;
        this.listener = listener;
        this.message = message;
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.view_cancel_booking_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();
        textViewCancelBookingMessage.setText(message);

    }

    private void setListeners() {
        textViewCancelBooking.setOnClickListener(this);
        textViewNoThanks.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewNoThanks:
                dismiss();
                break;

            case R.id.textViewCancelBooking: {
                listener.onCancelBooking(classModel);
                dismiss();
            }
            break;
        }
    }

    public interface OnCancelBooking {
        void onCancelBooking(ClassModel classModel);
    }
}
