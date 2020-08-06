// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.flexbox.FlexboxLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GetStartedActivity_ViewBinding implements Unbinder {
  private GetStartedActivity target;

  @UiThread
  public GetStartedActivity_ViewBinding(GetStartedActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public GetStartedActivity_ViewBinding(GetStartedActivity target, View source) {
    this.target = target;

    target.imageViewBack = Utils.findRequiredViewAsType(source, R.id.imageViewBack, "field 'imageViewBack'", ImageView.class);
    target.textViewSkip = Utils.findRequiredViewAsType(source, R.id.textViewSkip, "field 'textViewSkip'", TextView.class);
    target.textViewHeader = Utils.findRequiredViewAsType(source, R.id.textViewHeader, "field 'textViewHeader'", TextView.class);
    target.buttonBottom = Utils.findRequiredViewAsType(source, R.id.buttonBottom, "field 'buttonBottom'", Button.class);
    target.scrollView = Utils.findRequiredViewAsType(source, R.id.scrollView, "field 'scrollView'", ScrollView.class);
    target.flexBoxLayout = Utils.findRequiredViewAsType(source, R.id.flexBoxLayout, "field 'flexBoxLayout'", FlexboxLayout.class);
    target.flexBoxLayoutTime = Utils.findRequiredViewAsType(source, R.id.flexBoxLayoutTime, "field 'flexBoxLayoutTime'", FlexboxLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GetStartedActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewBack = null;
    target.textViewSkip = null;
    target.textViewHeader = null;
    target.buttonBottom = null;
    target.scrollView = null;
    target.flexBoxLayout = null;
    target.flexBoxLayoutTime = null;
  }
}
