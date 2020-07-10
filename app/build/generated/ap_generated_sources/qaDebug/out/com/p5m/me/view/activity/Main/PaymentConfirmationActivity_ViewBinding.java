// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PaymentConfirmationActivity_ViewBinding implements Unbinder {
  private PaymentConfirmationActivity target;

  @UiThread
  public PaymentConfirmationActivity_ViewBinding(PaymentConfirmationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PaymentConfirmationActivity_ViewBinding(PaymentConfirmationActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.imageViewPaymentStatus = Utils.findRequiredViewAsType(source, R.id.imageViewPaymentStatus, "field 'imageViewPaymentStatus'", ImageView.class);
    target.textViewPaymentStatus = Utils.findRequiredViewAsType(source, R.id.textViewPaymentStatus, "field 'textViewPaymentStatus'", TextView.class);
    target.buttonContactUs = Utils.findRequiredViewAsType(source, R.id.buttonContactUs, "field 'buttonContactUs'", Button.class);
    target.layoutPaymentStatus = Utils.findRequiredViewAsType(source, R.id.layoutPaymentStatus, "field 'layoutPaymentStatus'", LinearLayout.class);
    target.layoutConfirmation = Utils.findRequiredViewAsType(source, R.id.layoutConfirmation, "field 'layoutConfirmation'", LinearLayout.class);
    target.textViewPaymentReference = Utils.findRequiredViewAsType(source, R.id.textViewPaymentReference, "field 'textViewPaymentReference'", TextView.class);
    target.textViewBookedDate = Utils.findRequiredViewAsType(source, R.id.textViewBookedDate, "field 'textViewBookedDate'", TextView.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.appBarLayout, "field 'appBarLayout'", AppBarLayout.class);
    target.cardView = Utils.findRequiredViewAsType(source, R.id.cardView, "field 'cardView'", CardView.class);
    target.textViewPackageName = Utils.findRequiredViewAsType(source, R.id.textViewPackageName, "field 'textViewPackageName'", TextView.class);
    target.textViewClassName = Utils.findRequiredViewAsType(source, R.id.textViewClassName, "field 'textViewClassName'", TextView.class);
    target.textViewClass = Utils.findRequiredViewAsType(source, R.id.textViewClass, "field 'textViewClass'", TextView.class);
    target.textViewValidity = Utils.findRequiredViewAsType(source, R.id.textViewValidity, "field 'textViewValidity'", TextView.class);
    target.textViewAmount = Utils.findRequiredViewAsType(source, R.id.textViewAmount, "field 'textViewAmount'", TextView.class);
    target.textViewSubTitle = Utils.findRequiredViewAsType(source, R.id.textViewSubTitle, "field 'textViewSubTitle'", TextView.class);
    target.layoutValidity = Utils.findRequiredViewAsType(source, R.id.layoutValidity, "field 'layoutValidity'", LinearLayout.class);
    target.layoutNoData = Utils.findRequiredViewAsType(source, R.id.layoutNoData, "field 'layoutNoData'", LinearLayout.class);
    target.viewClass = Utils.findRequiredView(source, R.id.viewClass, "field 'viewClass'");
    target.layoutClass = Utils.findRequiredViewAsType(source, R.id.layoutClass, "field 'layoutClass'", LinearLayout.class);
    target.view = Utils.findRequiredView(source, R.id.view, "field 'view'");
    target.textViewPaymentDetail = Utils.findRequiredViewAsType(source, R.id.textViewPaymentDetail, "field 'textViewPaymentDetail'", TextView.class);
    target.textViewPackageTitle = Utils.findRequiredViewAsType(source, R.id.textViewPackageTitle, "field 'textViewPackageTitle'", TextView.class);
    target.buttonViewSchedule = Utils.findRequiredViewAsType(source, R.id.buttonViewSchedule, "field 'buttonViewSchedule'", Button.class);
    target.buttonTryAgain = Utils.findRequiredViewAsType(source, R.id.buttonTryAgain, "field 'buttonTryAgain'", Button.class);
    target.buttonInviteFriends = Utils.findRequiredViewAsType(source, R.id.buttonInviteFriends, "field 'buttonInviteFriends'", Button.class);
    target.buttonSchedule = Utils.findRequiredViewAsType(source, R.id.buttonSchedule, "field 'buttonSchedule'", Button.class);
    target.buttonsLayout = Utils.findRequiredViewAsType(source, R.id.buttonsLayout, "field 'buttonsLayout'", LinearLayout.class);
    target.progressBarDone = Utils.findRequiredViewAsType(source, R.id.progressBarDone, "field 'progressBarDone'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PaymentConfirmationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.imageViewPaymentStatus = null;
    target.textViewPaymentStatus = null;
    target.buttonContactUs = null;
    target.layoutPaymentStatus = null;
    target.layoutConfirmation = null;
    target.textViewPaymentReference = null;
    target.textViewBookedDate = null;
    target.appBarLayout = null;
    target.cardView = null;
    target.textViewPackageName = null;
    target.textViewClassName = null;
    target.textViewClass = null;
    target.textViewValidity = null;
    target.textViewAmount = null;
    target.textViewSubTitle = null;
    target.layoutValidity = null;
    target.layoutNoData = null;
    target.viewClass = null;
    target.layoutClass = null;
    target.view = null;
    target.textViewPaymentDetail = null;
    target.textViewPackageTitle = null;
    target.buttonViewSchedule = null;
    target.buttonTryAgain = null;
    target.buttonInviteFriends = null;
    target.buttonSchedule = null;
    target.buttonsLayout = null;
    target.progressBarDone = null;
  }
}
