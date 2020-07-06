// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ForceUpdateActivity_ViewBinding implements Unbinder {
  private ForceUpdateActivity target;

  @UiThread
  public ForceUpdateActivity_ViewBinding(ForceUpdateActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ForceUpdateActivity_ViewBinding(ForceUpdateActivity target, View source) {
    this.target = target;

    target.textViewTitle = Utils.findRequiredViewAsType(source, R.id.textViewTitle, "field 'textViewTitle'", TextView.class);
    target.textViewMessage = Utils.findRequiredViewAsType(source, R.id.textViewMessage, "field 'textViewMessage'", TextView.class);
    target.textViewUpdate = Utils.findRequiredViewAsType(source, R.id.textViewUpdate, "field 'textViewUpdate'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ForceUpdateActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewTitle = null;
    target.textViewMessage = null;
    target.textViewUpdate = null;
  }
}
