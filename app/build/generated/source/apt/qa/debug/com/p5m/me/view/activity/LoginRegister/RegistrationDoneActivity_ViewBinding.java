// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.LoginRegister;

import android.view.View;
import android.widget.Button;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RegistrationDoneActivity_ViewBinding implements Unbinder {
  private RegistrationDoneActivity target;

  private View view7f0a0079;

  @UiThread
  public RegistrationDoneActivity_ViewBinding(RegistrationDoneActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegistrationDoneActivity_ViewBinding(final RegistrationDoneActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.buttonDone, "field 'buttonDone' and method 'buttonDone'");
    target.buttonDone = Utils.castView(view, R.id.buttonDone, "field 'buttonDone'", Button.class);
    view7f0a0079 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.buttonDone(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    RegistrationDoneActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.buttonDone = null;

    view7f0a0079.setOnClickListener(null);
    view7f0a0079 = null;
  }
}
