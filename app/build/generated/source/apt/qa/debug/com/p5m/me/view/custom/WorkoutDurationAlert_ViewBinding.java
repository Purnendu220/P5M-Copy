// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.custom;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WorkoutDurationAlert_ViewBinding implements Unbinder {
  private WorkoutDurationAlert target;

  @UiThread
  public WorkoutDurationAlert_ViewBinding(WorkoutDurationAlert target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WorkoutDurationAlert_ViewBinding(WorkoutDurationAlert target, View source) {
    this.target = target;

    target.textViewTitle = Utils.findRequiredViewAsType(source, R.id.textViewTitle, "field 'textViewTitle'", TextView.class);
    target.textViewMessage = Utils.findRequiredViewAsType(source, R.id.textViewMessage, "field 'textViewMessage'", TextView.class);
    target.textViewCancel = Utils.findRequiredViewAsType(source, R.id.textViewCancel, "field 'textViewCancel'", TextView.class);
    target.textViewOk = Utils.findRequiredViewAsType(source, R.id.textViewOk, "field 'textViewOk'", TextView.class);
    target.linearLayoutRatePast = Utils.findRequiredViewAsType(source, R.id.linearLayoutRatePast, "field 'linearLayoutRatePast'", LinearLayout.class);
    target.imgAlertType = Utils.findRequiredViewAsType(source, R.id.img_alert_type, "field 'imgAlertType'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WorkoutDurationAlert target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewTitle = null;
    target.textViewMessage = null;
    target.textViewCancel = null;
    target.textViewOk = null;
    target.linearLayoutRatePast = null;
    target.imgAlertType = null;
  }
}
