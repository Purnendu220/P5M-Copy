// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.custom;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PackageExtensionAlertDialog_ViewBinding implements Unbinder {
  private PackageExtensionAlertDialog target;

  @UiThread
  public PackageExtensionAlertDialog_ViewBinding(PackageExtensionAlertDialog target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PackageExtensionAlertDialog_ViewBinding(PackageExtensionAlertDialog target, View source) {
    this.target = target;

    target.minusButton = Utils.findRequiredViewAsType(source, R.id.minusButton, "field 'minusButton'", ImageView.class);
    target.plusButton = Utils.findRequiredViewAsType(source, R.id.plusButton, "field 'plusButton'", ImageView.class);
    target.textViewCost = Utils.findRequiredViewAsType(source, R.id.textViewCost, "field 'textViewCost'", TextView.class);
    target.textViewValidUntil = Utils.findRequiredViewAsType(source, R.id.textViewValidUntil, "field 'textViewValidUntil'", TextView.class);
    target.textViewWeekValue = Utils.findRequiredViewAsType(source, R.id.textViewWeekValue, "field 'textViewWeekValue'", TextView.class);
    target.textViewCancel = Utils.findRequiredViewAsType(source, R.id.textViewCancel, "field 'textViewCancel'", TextView.class);
    target.textViewProceed = Utils.findRequiredViewAsType(source, R.id.textViewProceed, "field 'textViewProceed'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PackageExtensionAlertDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.minusButton = null;
    target.plusButton = null;
    target.textViewCost = null;
    target.textViewValidUntil = null;
    target.textViewWeekValue = null;
    target.textViewCancel = null;
    target.textViewProceed = null;
  }
}
