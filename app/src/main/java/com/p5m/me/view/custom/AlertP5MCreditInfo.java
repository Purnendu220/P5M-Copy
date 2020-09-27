package com.p5m.me.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.p5m.me.R;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.CreditValueLanguageModel;
import com.p5m.me.data.CreditValueModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.databinding.ViewP5mCreditInfoBinding;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.JsonUtils;
import com.p5m.me.utils.LanguageUtils;

import butterknife.ButterKnife;


public class AlertP5MCreditInfo extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ViewP5mCreditInfoBinding dataBinding;
    private OnAlertButtonAction alertButtonListener;
    private Object model;
    public AlertP5MCreditInfo(@NonNull Context context, Package pkg,  OnAlertButtonAction alertButtonListener) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.alertButtonListener = alertButtonListener;
        this.model = pkg;
        init();
    }
    private void init() {
        MixPanel.trackLearnAboutCredits();
        dataBinding = ViewP5mCreditInfoBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();
        dataBinding.linearLayoutButtons.setVisibility(View.VISIBLE);
        setDataForCredits();


    }
    private void setDataForCredits(){
        CreditValueModel model;
        String creditValueStr = RemoteConfigure.getFirebaseRemoteConfig(mContext).getRemoteConfigValue(RemoteConfigConst.CREDIT_VALUE_MODEL);
        CreditValueLanguageModel creditModel = JsonUtils.fromJson(creditValueStr, CreditValueLanguageModel.class);
        if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")){
            model = creditModel.getAr_values();
        }else{
            model = creditModel.getEn_values();

        }
        dataBinding.textViewAtGymClasses.setText(model.getAt_gym_title());
        dataBinding.textViewOnlineClasses.setText(model.getOnlline_title());
        dataBinding.textViewSpecialClasses.setText(model.getSpecial_title());

        dataBinding.textViewAtGymClassesCredit.setText(model.getAt_gym_credit());
        dataBinding.textViewOnlineClassesCredit.setText(model.getOnline_credit());
        dataBinding.textViewSpecialClassesCredit.setText(model.getSpecial_credit());

        dataBinding.textViewTitle.setText(model.getTitle());
        dataBinding.textViewCancel.setText(model.getCancel_text());
        dataBinding.textViewOk.setText(model.getOk_text());

    }

    private void setListeners() {
        dataBinding.textViewCancel.setOnClickListener(this);
        dataBinding.textViewOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewOk: {
                dismiss();
               // alertButtonListener.onOkClick(AppConstants.AlertRequestCodes.ALERT_REQUEST_PURCHASE,model);
            }
            break;
            case R.id.textViewCancel: {
                alertButtonListener.onCancelClick(AppConstants.AlertRequestCodes.ALERT_REQUEST_PURCHASE_CANCEL,"cancel");
                dismiss();
            }
            break;


        }

    }



}