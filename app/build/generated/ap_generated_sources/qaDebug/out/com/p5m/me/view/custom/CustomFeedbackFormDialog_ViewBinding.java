// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.custom;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CustomFeedbackFormDialog_ViewBinding implements Unbinder {
  private CustomFeedbackFormDialog target;

  @UiThread
  public CustomFeedbackFormDialog_ViewBinding(CustomFeedbackFormDialog target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CustomFeedbackFormDialog_ViewBinding(CustomFeedbackFormDialog target, View source) {
    this.target = target;

    target.textViewNoThanks = Utils.findRequiredViewAsType(source, R.id.textViewNoThanks, "field 'textViewNoThanks'", TextView.class);
    target.textViewCancelBooking = Utils.findRequiredViewAsType(source, R.id.textViewCancelBooking, "field 'textViewCancelBooking'", TextView.class);
    target.textViewCharLimit = Utils.findRequiredViewAsType(source, R.id.textViewCharLimit, "field 'textViewCharLimit'", TextView.class);
    target.editTextComment = Utils.findRequiredViewAsType(source, R.id.editTextComment, "field 'editTextComment'", EditText.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", LinearLayout.class);
    target.linearlayoutTopPart = Utils.findRequiredViewAsType(source, R.id.linearlayoutTopPart, "field 'linearlayoutTopPart'", LinearLayout.class);
    target.spinner = Utils.findRequiredViewAsType(source, R.id.spinner, "field 'spinner'", Spinner.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CustomFeedbackFormDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewNoThanks = null;
    target.textViewCancelBooking = null;
    target.textViewCharLimit = null;
    target.editTextComment = null;
    target.layout = null;
    target.linearlayoutTopPart = null;
    target.spinner = null;
  }
}
