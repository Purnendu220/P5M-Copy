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

public class WorkoutDurationAlert extends Dialog implements View.OnClickListener {

    public interface OnAlertButtonAction {

        void onOkClick(int requestCode, Object data);

        void onCancelClick(int requestCode, Object data);
    }


    private Context mContext;

    String buttonTitle,message,title;



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

    public WorkoutDurationAlert(@NonNull Context context, String title, String message,String buttonTitle) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.buttonTitle = buttonTitle;

        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.view_duration_alert);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
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
        if (buttonTitle != null && buttonTitle.length() > 0) {
            textViewOk.setText(buttonTitle);

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
            case R.id.textViewOk:
            case R.id.textViewCancel: {
                dismiss();

            }
            break;


        }

    }


}
