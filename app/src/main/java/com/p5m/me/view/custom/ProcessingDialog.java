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
import com.p5m.me.data.PaymentConfirmationResponse;
import com.p5m.me.data.SubscriptionConfigModal;
import com.p5m.me.data.UpdateSubscriptionRequest;
import com.p5m.me.data.UpdateSubscriptionResponse;
import com.p5m.me.data.main.Package;
import com.p5m.me.databinding.ViewP5mCreditInfoBinding;
import com.p5m.me.databinding.ViewProcessingLayoutBinding;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.JsonUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.view.activity.Main.PaymentConfirmationActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;


public class ProcessingDialog extends Dialog implements View.OnClickListener, NetworkCommunicator.RequestListener {
    private Context mContext;
    private ViewProcessingLayoutBinding dataBinding;
    private OnSubscriptionUpdate onSubscriptionUpdate;
    private UpdateSubscriptionRequest model;
    private NetworkCommunicator mNetworkCommunicator;
    private SubscriptionConfigModal subscriptionConfigModal;
    private Timer timer;
    public  int hitCount = 0;
    public  int maxHitCount = 6;

    public enum PaymentStatus {
        SUCCESS,
        FAILURE,
        PENDING,
        INITIALIZE
    }
    public ProcessingDialog(@NonNull Context context, UpdateSubscriptionRequest request, OnSubscriptionUpdate onSubscriptionUpdate) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.onSubscriptionUpdate = onSubscriptionUpdate;
        this.model = request;
        init();
    }
    private void init() {
        MixPanel.trackLearnAboutCredits();
        dataBinding = ViewProcessingLayoutBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setCancelable(false);
        mNetworkCommunicator = NetworkCommunicator.getInstance(mContext);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        dataBinding.processingLayout.setVisibility(View.VISIBLE);
        dataBinding.confirmationLayout.setVisibility(View.GONE);
        subscriptionConfigModal =  Helper.getSubscriptionConfig(mContext);
        dataBinding.textViewOk.setOnClickListener(this);
        subscriptionUpdateApi();
    }

   private void  subscriptionUpdateApi(){
       mNetworkCommunicator.updateSubscription(model,this);

   }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewOk:
            case R.id.textViewCancel: {
                dismiss();
                onSubscriptionUpdate.onFinishButtonClick(AppConstants.AlertRequestCodes.ALERT_REQUEST_PURCHASE);
            }
            break;


        }

    }


    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode){
            case NetworkCommunicator.RequestCode.UPDATE_SUBSCRIPTION:
                UpdateSubscriptionResponse update = (UpdateSubscriptionResponse) response;
                if(update.getIsCompleted()){
                    updateSuccessFull();
                }else{
                    if(update.getReferenceId()!=null){
                        startTimer(update.getReferenceId());

                    }else{
                        dataBinding.confirmationLayout.setVisibility(View.VISIBLE);
                        dataBinding.processingLayout.setVisibility(View.GONE);
                        dataBinding.textViewPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                        dataBinding.textViewPaymentStatus.setText(R.string.payment_pending);
                        dataBinding.imageViewPaymentStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pending));
                        dataBinding.linearLayoutButtons.setVisibility(View.VISIBLE);
                        dataBinding.textViewTitle.setText(mContext.getResources().getString(R.string.payment_pending));
                        onSubscriptionUpdate.onUpdateSuccess(model,PaymentStatus.PENDING);

                    }

                }
                break;
            case NetworkCommunicator.RequestCode.PAYMENT_CONFIRMATION_DETAIL:
                PaymentConfirmationResponse paymentResponse = ((ResponseModel<PaymentConfirmationResponse>) response).data;
                PaymentStatus status = PaymentStatus.valueOf(paymentResponse.getStatus());

                switch (status){
                    case SUCCESS:
                        timer.cancel();
                        updateSuccessFull();

                        break;
                    case PENDING:
                    case INITIALIZE:
                        if(hitCount==maxHitCount){
                            timer.cancel();
                            dataBinding.confirmationLayout.setVisibility(View.VISIBLE);
                            dataBinding.processingLayout.setVisibility(View.GONE);
                            dataBinding.textViewPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.payment_pending));
                            dataBinding.textViewPaymentStatus.setText(R.string.payment_pending);
                            dataBinding.imageViewPaymentStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.pending));
                            dataBinding.linearLayoutButtons.setVisibility(View.VISIBLE);
                            dataBinding.textViewTitle.setText(mContext.getResources().getString(R.string.payment_pending));
                            onSubscriptionUpdate.onUpdateSuccess(model,PaymentStatus.PENDING);

                        }

                        break;
                    case FAILURE:
                        timer.cancel();
                        dataBinding.confirmationLayout.setVisibility(View.VISIBLE);
                        dataBinding.processingLayout.setVisibility(View.GONE);
                        dataBinding.textViewPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.wewak));
                        dataBinding.textViewPaymentStatus.setText(R.string.payment_unsucessful);
                        dataBinding.imageViewPaymentStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.failed));
                        dataBinding.linearLayoutButtons.setVisibility(View.VISIBLE);
                        dataBinding.textViewTitle.setText(mContext.getResources().getString(R.string.payment_unsucessful));
                        onSubscriptionUpdate.onUpdateSuccess(model,PaymentStatus.FAILURE);

                        break;

                }

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode){
            case NetworkCommunicator.RequestCode.UPDATE_SUBSCRIPTION:
                dataBinding.textViewTitle.setText(mContext.getResources().getString(R.string.payment_unsucessful));

                dataBinding.confirmationLayout.setVisibility(View.VISIBLE);
                dataBinding.processingLayout.setVisibility(View.GONE);
                dataBinding.textViewPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.wewak));
                dataBinding.textViewPaymentStatus.setText(R.string.payment_unsucessful);
                dataBinding.imageViewPaymentStatus.setImageDrawable(mContext.getResources().getDrawable(R.drawable.failed));
                dataBinding.linearLayoutButtons.setVisibility(View.VISIBLE);
                onSubscriptionUpdate.onUpdateSuccess(model,PaymentStatus.FAILURE);


                break;
            case NetworkCommunicator.RequestCode.PAYMENT_CONFIRMATION_DETAIL:
                break;

        }
    }

   private void updateSuccessFull(){
        onSubscriptionUpdate.onUpdateSuccess(model,PaymentStatus.SUCCESS);
        dataBinding.textViewTitle.setText(mContext.getResources().getString(R.string.payment_sucessful));
       dataBinding.confirmationLayout.setVisibility(View.VISIBLE);
       dataBinding.processingLayout.setVisibility(View.GONE);
       dataBinding.linearLayoutButtons.setVisibility(View.VISIBLE);
       dataBinding.textViewPaymentStatus.setTextColor(mContext.getResources().getColor(R.color.green));

        if(model.getAction().equalsIgnoreCase(AppConstants.SubscriptionAction.EXTEND)){
            dataBinding.textViewPaymentStatus.setText(subscriptionConfigModal.getPackageExtendMessageSuccess());
        }
       if(model.getAction().equalsIgnoreCase(AppConstants.SubscriptionAction.RENEW)){
           dataBinding.textViewPaymentStatus.setText(subscriptionConfigModal.getRenewSubscriptionSuccess());

       }
       if(model.getAction().equalsIgnoreCase(AppConstants.SubscriptionAction.UPGRADE)){
           dataBinding.textViewPaymentStatus.setText(subscriptionConfigModal.getUpdateSubscriptionSuccess());
       }
   }
  private void startTimer(String refrenceId){
       timer = new Timer();
       timer.scheduleAtFixedRate( new FindPaymentStatus(refrenceId), 10000, 10000 );
  }

    private class FindPaymentStatus extends TimerTask {
       String refrenceId;
        public FindPaymentStatus(String refrenceId) {
            this.refrenceId = refrenceId;
            hitCount++;
        }

        @Override
        public void run() {
           mNetworkCommunicator.getPaymentDetail(Long.parseLong(refrenceId),ProcessingDialog.this,false);
        }
    }
}