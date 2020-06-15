// Generated code from Butter Knife. Do not modify!
package com.p5m.me.helper;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CancelBookingBottomDialogFragment_ViewBinding implements Unbinder {
  private CancelBookingBottomDialogFragment target;

  @UiThread
  public CancelBookingBottomDialogFragment_ViewBinding(CancelBookingBottomDialogFragment target,
      View source) {
    this.target = target;

    target.textViewCancelFriendBooking = Utils.findRequiredViewAsType(source, R.id.textViewCancelFriendBooking, "field 'textViewCancelFriendBooking'", TextView.class);
    target.textViewDialogDismiss = Utils.findRequiredViewAsType(source, R.id.textViewDialogDismiss, "field 'textViewDialogDismiss'", TextView.class);
    target.textViewCancelBothBooking = Utils.findRequiredViewAsType(source, R.id.textViewCancelBothBooking, "field 'textViewCancelBothBooking'", TextView.class);
    target.textViewCancelBooking = Utils.findRequiredViewAsType(source, R.id.textViewCancelBooking, "field 'textViewCancelBooking'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CancelBookingBottomDialogFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewCancelFriendBooking = null;
    target.textViewDialogDismiss = null;
    target.textViewCancelBothBooking = null;
    target.textViewCancelBooking = null;
  }
}
