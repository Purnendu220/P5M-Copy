// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.LoginRegister;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ContinueUser_ViewBinding implements Unbinder {
  private ContinueUser target;

  @UiThread
  public ContinueUser_ViewBinding(ContinueUser target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ContinueUser_ViewBinding(ContinueUser target, View source) {
    this.target = target;

    target.buttonContinue = Utils.findRequiredViewAsType(source, R.id.buttonContinue, "field 'buttonContinue'", Button.class);
    target.buttonLogin = Utils.findRequiredViewAsType(source, R.id.buttonLogin, "field 'buttonLogin'", Button.class);
    target.buttonRegister = Utils.findRequiredViewAsType(source, R.id.buttonRegister, "field 'buttonRegister'", Button.class);
    target.textViewSwitch = Utils.findRequiredViewAsType(source, R.id.textViewSwitch, "field 'textViewSwitch'", TextView.class);
    target.imageView = Utils.findRequiredViewAsType(source, R.id.imageView, "field 'imageView'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ContinueUser target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.buttonContinue = null;
    target.buttonLogin = null;
    target.buttonRegister = null;
    target.textViewSwitch = null;
    target.imageView = null;
  }
}
