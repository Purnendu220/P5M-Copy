// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.custom;

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

public class CustomDialogThankYou_ViewBinding implements Unbinder {
  private CustomDialogThankYou target;

  @UiThread
  public CustomDialogThankYou_ViewBinding(CustomDialogThankYou target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CustomDialogThankYou_ViewBinding(CustomDialogThankYou target, View source) {
    this.target = target;

    target.textViewNotNow = Utils.findRequiredViewAsType(source, R.id.textViewNotNow, "field 'textViewNotNow'", TextView.class);
    target.textViewIWillDoLater = Utils.findRequiredViewAsType(source, R.id.textViewIWillDoLater, "field 'textViewIWillDoLater'", TextView.class);
    target.textViewRatePrevious = Utils.findRequiredViewAsType(source, R.id.textViewRatePrevious, "field 'textViewRatePrevious'", TextView.class);
    target.linearLayoutRatePast = Utils.findRequiredViewAsType(source, R.id.linearLayoutRatePast, "field 'linearLayoutRatePast'", LinearLayout.class);
    target.layoutButtons = Utils.findRequiredViewAsType(source, R.id.layoutButtons, "field 'layoutButtons'", LinearLayout.class);
    target.textViewGotIt = Utils.findRequiredViewAsType(source, R.id.textViewGotIt, "field 'textViewGotIt'", TextView.class);
    target.textViewReveiwSubmitted = Utils.findRequiredViewAsType(source, R.id.textViewReveiwSubmitted, "field 'textViewReveiwSubmitted'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CustomDialogThankYou target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewNotNow = null;
    target.textViewIWillDoLater = null;
    target.textViewRatePrevious = null;
    target.linearLayoutRatePast = null;
    target.layoutButtons = null;
    target.textViewGotIt = null;
    target.textViewReveiwSubmitted = null;
  }
}
