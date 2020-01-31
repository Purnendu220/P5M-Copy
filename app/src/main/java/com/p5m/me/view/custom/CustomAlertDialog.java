package com.p5m.me.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.p5m.me.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CustomAlertDialog extends Dialog implements View.OnClickListener {

    public interface OnAlertButtonAction {

        void onOkClick(int requestCode, Object data);

        void onCancelClick(int requestCode, Object data);
    }


    private Context mContext;
    private String title;
    private int SUCCESS = 1;
    private int FAIL = 0;
    private int WARNING = 2;
    String message;
    int alertType = 1;
    String buttonCancelText;
    String buttonOkText;
    int callFor;
    OnAlertButtonAction alertButtonListener;
    Object data;
    boolean isCancelable = false;


    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewMessage)
    TextView textViewMessage;

    @BindView(R.id.textViewCancel)
    TextView textViewCancel;

    @BindView(R.id.textViewOk)
    TextView textViewOk;

    @BindView(R.id.linearLayoutRatePast)
    LinearLayout linearLayoutRatePast;

    @BindView(R.id.img_alert_type)
    ImageView imgAlertType;

    public CustomAlertDialog(@NonNull Context context, String title, String message, int alertType, String buttonCancelText, String buttonOkText, int callFor, Object data, boolean isCancelable, OnAlertButtonAction alertButtonListener) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.alertType = alertType;
        this.buttonCancelText = buttonCancelText;
        this.buttonOkText = buttonOkText;
        this.callFor = callFor;
        this.alertButtonListener = alertButtonListener;
        this.data = data;
        this.isCancelable = isCancelable;
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.view_custom_alert);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(isCancelable);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        if (title != null && title.length() > 0) {
            textViewTitle.setText(title);
        }
        if (message != null && message.length() > 0) {
            textViewMessage.setText(message);

        }

        if (buttonCancelText != null && buttonCancelText.length() > 0) {
            textViewCancel.setVisibility(View.VISIBLE);
            textViewCancel.setText(buttonCancelText);
        } else {
            textViewCancel.setVisibility(View.GONE);
        }
        if (buttonOkText != null && buttonOkText.length() > 0) {
            textViewOk.setVisibility(View.VISIBLE);
            textViewOk.setText(buttonOkText);

        } else {
            textViewOk.setVisibility(View.GONE);
        }

        setListeners();
        linearLayoutRatePast.setVisibility(View.VISIBLE);


    }

    private void setListeners() {
        textViewCancel.setOnClickListener(this);
        textViewOk.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewOk: {
                dismiss();
                if (alertButtonListener != null)
                    alertButtonListener.onOkClick(callFor, data);
            }
            break;
            case R.id.textViewCancel: {
                dismiss();
                if (alertButtonListener != null)
                    alertButtonListener.onCancelClick(callFor, data);
            }
            break;


        }

    }

    public interface AlertRequestCodes {
        int ALERT_REQUEST_SUCCESSFULL_UNJOIN = 1;
        int ALERT_REQUEST_WALLET_INFO = 2;


    }

}
