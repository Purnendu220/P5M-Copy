package com.p5m.me.view.custom;


import android.app.Activity;
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
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.view.activity.LoginRegister.InfoScreen;


import butterknife.BindView;
import butterknife.ButterKnife;


public class CustomDialogThankYou extends Dialog implements OnClickListener {


    private final int navigatinFrom;
    private boolean isThereOtherClassToRate;
    private Context mContext;
    @BindView(R.id.textViewNotNow)
    TextView textViewNotNow;

    @BindView(R.id.textViewIWillDoLater)
    TextView textViewIWillDoLater;

    @BindView(R.id.textViewRatePrevious)
    TextView textViewRatePrevious;

    @BindView(R.id.linearLayoutRatePast)
    LinearLayout linearLayoutRatePast;
    @BindView(R.id.layoutButtons)
    LinearLayout layoutButtons;
    @BindView(R.id.textViewGotIt)
    TextView textViewGotIt;
    @BindView(R.id.textViewReveiwSubmitted)
    TextView textViewReveiwSubmitted;

    public CustomDialogThankYou(@NonNull Context context, boolean isThereOtherClassToRate, int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.isThereOtherClassToRate = isThereOtherClassToRate;
        this.navigatinFrom = navigatinFrom;
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.view_thank_you_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();
        if (isThereOtherClassToRate) {
            linearLayoutRatePast.setVisibility(View.VISIBLE);
            textViewNotNow.setVisibility(View.GONE);
        } else {
            linearLayoutRatePast.setVisibility(View.GONE);
            textViewNotNow.setVisibility(View.VISIBLE);
        }

        if (navigatinFrom == AppConstants.AppNavigation.NAVIGATION_FROM_OTHER_USER) {
            layoutButtons.setVisibility(View.GONE);
            textViewGotIt.setVisibility(View.VISIBLE);
            textViewNotNow.setText(context.getString(R.string.ok));
            textViewReveiwSubmitted.setText(context.getString(R.string.thank_you_submit_request));
        } else {
            layoutButtons.setVisibility(View.VISIBLE);
            textViewGotIt.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        textViewNotNow.setOnClickListener(this);
        textViewIWillDoLater.setOnClickListener(this);
        textViewRatePrevious.setOnClickListener(this);
        textViewGotIt.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewGotIt:
            case R.id.textViewNotNow:
            case R.id.textViewIWillDoLater:
                if (navigatinFrom == AppConstants.AppNavigation.NAVIGATION_FROM_OTHER_USER) {
                    InfoScreen.open(mContext);
                } else
                    dismiss();

                break;

            case R.id.textViewRatePrevious: {
                if (navigatinFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS) {
                    RefrenceWrapper.getRefrenceWrapper(mContext).getActivity().navigateToMyProfile();
                }
                dismiss();

            }
            break;

        }

    }


}
