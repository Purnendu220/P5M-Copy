package com.p5m.me.view.custom;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.view.activity.Main.FullRatingActivity;
import com.p5m.me.view.activity.Main.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CustomDialogThankYou extends Dialog implements OnClickListener {


    private final int navigatinFrom;
    private  boolean isThereOtherClassToRate;
    private  Context mContext;
    @BindView(R.id.textViewNotNow)
    TextView textViewNotNow;

    @BindView(R.id.textViewIWillDoLater)
    TextView textViewIWillDoLater;

    @BindView(R.id.textViewRatePrevious)
    TextView textViewRatePrevious;

    @BindView(R.id.linearLayoutRatePast)
    LinearLayout linearLayoutRatePast;

    public CustomDialogThankYou(@NonNull Context context, boolean isThereOtherClassToRate,int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext=context;
        this.isThereOtherClassToRate=isThereOtherClassToRate;
        this.navigatinFrom=navigatinFrom;
        init(context);
    }
    private void init(Context context) {
        setContentView(R.layout.view_thank_you_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();
        if(isThereOtherClassToRate){
            linearLayoutRatePast.setVisibility(View.VISIBLE);
            textViewNotNow.setVisibility(View.GONE);
        }else{
            linearLayoutRatePast.setVisibility(View.GONE);
            textViewNotNow.setVisibility(View.VISIBLE);
        }

    }

    private void setListeners(){
        textViewNotNow.setOnClickListener(this);
        textViewIWillDoLater.setOnClickListener(this);
        textViewRatePrevious.setOnClickListener(this);



    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textViewNotNow:{
                dismiss();
                }
            break;
            case R.id.textViewIWillDoLater:{
                dismiss();

            }
            break;
            case R.id.textViewRatePrevious:{
                if(navigatinFrom== AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS){
                    RefrenceWrapper.getRefrenceWrapper(mContext).getActivity().navigateToMyProfile();
                }
                dismiss();

            }
            break;

        }

    }


}
