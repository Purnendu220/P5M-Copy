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

import com.p5m.me.data.SpecialProgramPopupModel;
import com.p5m.me.data.SpecialProgramValuesModel;
import com.p5m.me.data.UserDetail;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.User;
import com.p5m.me.databinding.ViewSpecialProgramLayoutBinding;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.JsonUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.view.activity.Main.TrainerProfileActivity;
import com.p5m.me.view.activity.Main.WebViewActivity;

import butterknife.ButterKnife;

public class SpecialProgramPopup extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ViewSpecialProgramLayoutBinding dataBinding;
    private OnAlertButtonAction alertButtonListener;
    private ClassModel classModel;
    private int type;
    private User user;
    SpecialProgramPopupModel specialProgramModel;

    public SpecialProgramPopup(@NonNull Context context, ClassModel classModel,int type, OnAlertButtonAction alertButtonListener) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.alertButtonListener = alertButtonListener;
        this.classModel = classModel;
        this.type = type;
        this.user = TempStorage.getUser();
        init();
    }
    private void init() {
        dataBinding = ViewSpecialProgramLayoutBinding.inflate(getLayoutInflater());
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
        SpecialProgramValuesModel model;
        String creditValueStr = RemoteConfigure.getFirebaseRemoteConfig(mContext).getRemoteConfigValue(RemoteConfigConst.SPECIAL_PROGRAM_POPUP);
        specialProgramModel = JsonUtils.fromJson(creditValueStr, SpecialProgramPopupModel.class);
        if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")){
            model = specialProgramModel.getArValues();
        }else{
            model = specialProgramModel.getEnValues();

        }
        if(type==0){
            dataBinding.textViewMessage.setText(model.getMessageNoPlan());
            dataBinding.textViewTitle.setText(model.getTitle());
            dataBinding.textViewCancel.setText(model.getButtonOneNoPlan());
            dataBinding.textViewOk.setText(model.getButtonTwoNoPlan());
        }else{
            dataBinding.textViewMessage.setText(model.getMessageClassFull());
            dataBinding.textViewTitle.setText(model.getTitle());
            dataBinding.textViewCancel.setText(model.getButtonOneClassFull());
            dataBinding.textViewOk.setText(model.getButtonTwoClassFull());

        }


    }

    private void setListeners() {
        dataBinding.textViewCancel.setOnClickListener(this);
        dataBinding.textViewOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewOk: {
                String url = specialProgramModel.getUrl()+"?email="+TempStorage.getUser().getEmail()+"&name="+TempStorage.getUser().getFirstName();
                WebViewActivity.open(mContext,url,true);
                dismiss();
                // alertButtonListener.onOkClick(AppConstants.AlertRequestCodes.ALERT_REQUEST_PURCHASE,model);
            }
            break;
            case R.id.textViewCancel: {
                if(type==0){
                    //no plan or plan
                    int creditRequiredForClass =  Helper.requiredCreditForClass(TempStorage.getUser(),classModel,1);
                    UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());
                    if(!userPackageInfo.haveGeneralPackage){
// no plan
                        alertButtonListener.onCancelClick(AppConstants.AlertRequestCodes.ALERT_REQUEST_NO_PLAN,"cancel");
                    }
                    else if(userPackageInfo.haveGeneralPackage&&creditRequiredForClass>userPackageInfo.userPackageGeneral.getBalance()){
// insufficient Credits
            alertButtonListener.onCancelClick(AppConstants.AlertRequestCodes.ALERT_REQUEST_NO_PLAN,"cancel");
                    }
                    else{
                        // sufficient Credits
                        alertButtonListener.onCancelClick(AppConstants.AlertRequestCodes.ALERT_REQUEST_HAVE_PLAN,"cancel");

                    }
                }else{
                    TrainerProfileActivity.open(mContext,classModel.getTrainerDetail().getId(),AppConstants.AppNavigation.NAVIGATION_FROM_SPECIAL_PROGRAM);
                }
                dismiss();
            }
            break;


        }

    }



}