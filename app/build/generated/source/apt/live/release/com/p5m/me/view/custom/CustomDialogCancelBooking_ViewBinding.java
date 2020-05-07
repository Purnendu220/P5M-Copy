// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.custom;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CustomDialogCancelBooking_ViewBinding implements Unbinder {
  private CustomDialogCancelBooking target;

  @UiThread
  public CustomDialogCancelBooking_ViewBinding(CustomDialogCancelBooking target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CustomDialogCancelBooking_ViewBinding(CustomDialogCancelBooking target, View source) {
    this.target = target;

    target.textViewCancelBookingMessage = Utils.findRequiredViewAsType(source, R.id.textViewCancelBookingMessage, "field 'textViewCancelBookingMessage'", TextView.class);
    target.textViewNoRefund = Utils.findRequiredViewAsType(source, R.id.textViewNoRefund, "field 'textViewNoRefund'", TextView.class);
    target.textViewNoThanks = Utils.findRequiredViewAsType(source, R.id.textViewNoThanks, "field 'textViewNoThanks'", TextView.class);
    target.textViewCancelBooking = Utils.findRequiredViewAsType(source, R.id.textViewCancelBooking, "field 'textViewCancelBooking'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CustomDialogCancelBooking target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewCancelBookingMessage = null;
    target.textViewNoRefund = null;
    target.textViewNoThanks = null;
    target.textViewCancelBooking = null;
  }
}
