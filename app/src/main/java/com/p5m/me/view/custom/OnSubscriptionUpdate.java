package com.p5m.me.view.custom;

import com.p5m.me.data.UpdateSubscriptionRequest;

public interface OnSubscriptionUpdate {
    void  onUpdateSuccess(UpdateSubscriptionRequest request, ProcessingDialog.PaymentStatus status);
    void onFinishButtonClick(int type);
}
