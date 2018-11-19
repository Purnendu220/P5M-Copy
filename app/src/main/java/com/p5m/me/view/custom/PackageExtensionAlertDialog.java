package com.p5m.me.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.data.ValidityPackageList;
import com.p5m.me.data.main.DefaultSettingServer;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.CheckoutActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class PackageExtensionAlertDialog extends Dialog implements View.OnClickListener {


    private final int navigatinFrom;
    private Context mContext;
    private UserPackage userPackage;

    @BindView(R.id.minusButton)
    ImageView minusButton;

    @BindView(R.id.plusButton)
    ImageView plusButton;

    @BindView(R.id.textViewCost)
    TextView textViewCost;

    @BindView(R.id.textViewValidUntil)
    TextView textViewValidUntil;

    @BindView(R.id.textViewWeekValue)
    TextView textViewWeekValue;

    @BindView(R.id.textViewCancel)
    TextView textViewCancel;

    @BindView(R.id.textViewProceed)
    TextView textViewProceed;

    private int weekValue=1;
    public List<ValidityPackageList> defaultServerPacakageList;
    public ValidityPackageList selectedPacakageFromList;
    private int weeksExtensionAllowed=4;




    public PackageExtensionAlertDialog(@NonNull Context context,int navigatinFrom,UserPackage user) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext=context;
        this.navigatinFrom=navigatinFrom;
        this.userPackage=user;
        init(context);
    }
    private void init(Context context) {
        setContentView(R.layout.view_package_extension_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        defaultServerPacakageList=TempStorage.getDefault().getValidityPackageList();
        setweeksAllowedToExtend();
        setListeners();
        handlePlusMinus(weekValue);
    }
    private void setweeksAllowedToExtend(){
        if(userPackage.getTotalRemainingWeeks()!=null&&userPackage.getTotalRemainingWeeks()>0){
            weeksExtensionAllowed=userPackage.getTotalRemainingWeeks();
            return;
        }
        if(defaultServerPacakageList!=null&&defaultServerPacakageList.size()>0){
            weeksExtensionAllowed=defaultServerPacakageList.size();
            return;
        }

    }

    private void setListeners(){
     minusButton.setOnClickListener(this);
     plusButton.setOnClickListener(this);
     textViewCancel.setOnClickListener(this);
     textViewProceed.setOnClickListener(this);


        }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.minusButton:{
                if(weekValue!=1){
                    weekValue=weekValue-1;
                    handlePlusMinus(weekValue);
                    }
            }
            break;
            case R.id.plusButton:{
                if(weekValue < weeksExtensionAllowed){
                    weekValue=weekValue+1;
                    handlePlusMinus(weekValue);
                }
                else{
                    ToastUtils.show(mContext,"You can extend your package upto "+weeksExtensionAllowed+" weeks");
                }

            }
            break;
            case R.id.textViewCancel:{
                dismiss();


            }
            break;
            case R.id.textViewProceed:{
                dismiss();
                CheckoutActivity.openActivity(mContext, selectedPacakageFromList,userPackage,navigatinFrom);


            }
            break;

        }

    }

    private void handlePlusMinus(int weekVakueSelected){
        if(defaultServerPacakageList!=null && defaultServerPacakageList.size()>0){
            for (ValidityPackageList selectedPacakageData:defaultServerPacakageList) {
                if(selectedPacakageData.getDuration()==weekVakueSelected){
                    selectedPacakageFromList=selectedPacakageData;
                }
            }
            if(selectedPacakageFromList!=null){
                textViewCost.setText(selectedPacakageFromList.getCost()+" KWD");
                textViewWeekValue.setText(selectedPacakageFromList.getDuration()+" Week(s)");
                String validUntill=DateUtils.getExtendedExpiryDate(userPackage.getExpiryDate(),selectedPacakageFromList.getDuration());
                String message=String.format(mContext.getString(R.string.valid_intil),validUntill);
                textViewValidUntil.setText(message);
            }

        }else{
          dismiss();
        }



    }



}
