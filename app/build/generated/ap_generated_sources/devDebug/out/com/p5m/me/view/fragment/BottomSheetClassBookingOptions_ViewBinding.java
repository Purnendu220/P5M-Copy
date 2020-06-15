// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BottomSheetClassBookingOptions_ViewBinding implements Unbinder {
  private BottomSheetClassBookingOptions target;

  @UiThread
  public BottomSheetClassBookingOptions_ViewBinding(BottomSheetClassBookingOptions target,
      View source) {
    this.target = target;

    target.textViewDropIn = Utils.findRequiredViewAsType(source, R.id.textViewDropIn, "field 'textViewDropIn'", TextView.class);
    target.textViewDropInDesc = Utils.findRequiredViewAsType(source, R.id.textViewDropInDesc, "field 'textViewDropInDesc'", TextView.class);
    target.textViewBuyMembership = Utils.findRequiredViewAsType(source, R.id.textViewBuyMembership, "field 'textViewBuyMembership'", TextView.class);
    target.textViewBuyMembershipDesc = Utils.findRequiredViewAsType(source, R.id.textViewBuyMembershipDesc, "field 'textViewBuyMembershipDesc'", TextView.class);
    target.textViewDialogDismiss = Utils.findRequiredViewAsType(source, R.id.textViewDialogDismiss, "field 'textViewDialogDismiss'", TextView.class);
    target.bookWithP5mMembership = Utils.findRequiredViewAsType(source, R.id.bookWithP5mMembership, "field 'bookWithP5mMembership'", LinearLayout.class);
    target.bookWithDropInPackage = Utils.findRequiredViewAsType(source, R.id.bookWithDropInPackage, "field 'bookWithDropInPackage'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BottomSheetClassBookingOptions target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewDropIn = null;
    target.textViewDropInDesc = null;
    target.textViewBuyMembership = null;
    target.textViewBuyMembershipDesc = null;
    target.textViewDialogDismiss = null;
    target.bookWithP5mMembership = null;
    target.bookWithDropInPackage = null;
  }
}
