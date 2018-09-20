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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.request.ClassRatingRequest;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.activity.Main.FullRatingActivity;
import com.p5m.me.view.activity.Main.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomRateAlertDialog extends Dialog implements OnClickListener, NetworkCommunicator.RequestListener {
    private Context mContext;
    @BindView(R.id.linearlayoutTopPart)
    public LinearLayout linearlayoutTopPart;

    @BindView(R.id.textViewHowWasClass)
    public TextView textViewHowWasClass;

    @BindView(R.id.textViewRatingCount)
    public TextView textViewRatingCount;

    @BindView(R.id.textViewNotNow)
    public TextView textViewNotNow;

    @BindView(R.id.ratingStar1)
    public ImageView ratingStar1;

    @BindView(R.id.ratingStar2)
    public ImageView ratingStar2;

    @BindView(R.id.ratingStar3)
    public ImageView ratingStar3;

    @BindView(R.id.ratingStar4)
    public ImageView ratingStar4;

    @BindView(R.id.ratingStar5)
    public ImageView ratingStar5;

    ClassModel model;
    private int navigatedFrom;
    private NetworkCommunicator networkCommunicator;


    public CustomRateAlertDialog(@NonNull Context context,@NonNull ClassModel model,int navigatedFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext=context;
        this.model=model;
        this.navigatedFrom=navigatedFrom;
        init(context);
    }
    private void init(Context context) {
       requestWindowFeature(Window.FEATURE_NO_TITLE); //before

        setContentView(R.layout.dialog_rate_class);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ButterKnife.bind(this);
        setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        CharSequence text = String.format(mContext.getString(R.string.how_was_your_class),model.getTitle());
        textViewHowWasClass.setText(text);
        setListeners();
        networkCommunicator = NetworkCommunicator.getInstance(context);


    }

   private void setListeners(){
       textViewNotNow.setOnClickListener(this);
       ratingStar1.setOnClickListener(this);
       ratingStar2.setOnClickListener(this);
       ratingStar3.setOnClickListener(this);
       ratingStar4.setOnClickListener(this);
       ratingStar5.setOnClickListener(this);


   }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textViewNotNow:{
                if(navigatedFrom== AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS){
                    ClassRatingRequest request =new ClassRatingRequest();
                    request.setmObjectTypeId(5);
                    request.setmObjectDataId(model.getClassSessionId());
                    request.setmUserId(TempStorage.getUser().getId());
                    request.setIsPublish(0);
                    request.setStatus(3);
                    networkCommunicator.submitClassRating(request,this,false);
                }
                dismiss();

            }
            break;
            case R.id.ratingStar1:{
                setStarRating(1);
            }
            break;
            case R.id.ratingStar2: {
                setStarRating(2);
            }
            break;
            case R.id.ratingStar3:{
                setStarRating(3);
                }
            break;
            case R.id.ratingStar4:{
                setStarRating(4);
                }
            break;
            case R.id.ratingStar5:{
                setStarRating(5);
                }
            break;
        }

    }
    public void setStarRatingsFilled(Context context, View... view) {
        for (View v : view) {
                ((ImageView) v).setImageResource(R.drawable.star_filled);
        }
    }
    public void setStarRatingsBlank(Context context, View... view) {
        for (View v : view) {
            ((ImageView) v).setImageResource(R.drawable.star_blank);
        }
    }
    private void setStarRating(int rating){
        switch (rating){
            case 1:
                setStarRatingsFilled(mContext,ratingStar1);
                setStarRatingsBlank(mContext,ratingStar2,ratingStar3,ratingStar4,ratingStar5);
                break;
            case 2:
                setStarRatingsFilled(mContext,ratingStar1,ratingStar2);
                setStarRatingsBlank(mContext,ratingStar3,ratingStar4,ratingStar5);
                break;
            case 3:
                setStarRatingsFilled(mContext,ratingStar1,ratingStar2,ratingStar3);
                setStarRatingsBlank(mContext,ratingStar4,ratingStar5);
                break;
            case 4:
                setStarRatingsFilled(mContext,ratingStar1,ratingStar2,ratingStar3,ratingStar4);
                setStarRatingsBlank(mContext,ratingStar5);
                break;
            case 5:
                setStarRatingsFilled(mContext,ratingStar1,ratingStar2,ratingStar3,ratingStar4,ratingStar5);
                break;
        }
        textViewRatingCount.setText(rating+" of 5 star");
        FullRatingActivity.open(mContext,navigatedFrom,model,rating);


    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

    }
}
