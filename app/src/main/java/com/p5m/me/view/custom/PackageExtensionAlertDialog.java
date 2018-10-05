package com.p5m.me.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.RefrenceWrapper;

import butterknife.BindView;
import butterknife.ButterKnife;



public class PackageExtensionAlertDialog extends Dialog implements View.OnClickListener {


    private final int navigatinFrom;
    private Context mContext;


    public PackageExtensionAlertDialog(@NonNull Context context,int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext=context;
        this.navigatinFrom=navigatinFrom;
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
        setListeners();


    }

    private void setListeners(){


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
