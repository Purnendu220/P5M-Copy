// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.LoginRegister;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ForgotPasswordActivity_ViewBinding implements Unbinder {
  private ForgotPasswordActivity target;

  @UiThread
  public ForgotPasswordActivity_ViewBinding(ForgotPasswordActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ForgotPasswordActivity_ViewBinding(ForgotPasswordActivity target, View source) {
    this.target = target;

    target.imageViewBack = Utils.findRequiredViewAsType(source, R.id.imageViewBack, "field 'imageViewBack'", ImageView.class);
    target.editTextEmail = Utils.findRequiredViewAsType(source, R.id.editTextEmail, "field 'editTextEmail'", EditText.class);
    target.textInputLayoutEmail = Utils.findRequiredViewAsType(source, R.id.textInputLayoutEmail, "field 'textInputLayoutEmail'", TextInputLayout.class);
    target.buttonReset = Utils.findRequiredViewAsType(source, R.id.buttonReset, "field 'buttonReset'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ForgotPasswordActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewBack = null;
    target.editTextEmail = null;
    target.textInputLayoutEmail = null;
    target.buttonReset = null;
  }
}
