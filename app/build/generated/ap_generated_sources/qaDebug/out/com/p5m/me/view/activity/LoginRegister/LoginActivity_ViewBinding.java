// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.LoginRegister;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  private View view7f0a0416;

  private View view7f0a0470;

  private View view7f0a0080;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(final LoginActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.textViewForgetPassword, "field 'textViewForgetPassword' and method 'textViewForgetPassword'");
    target.textViewForgetPassword = Utils.castView(view, R.id.textViewForgetPassword, "field 'textViewForgetPassword'", TextView.class);
    view7f0a0416 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.textViewForgetPassword(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.textViewSignUp, "field 'textViewSignUp' and method 'textViewSignUp'");
    target.textViewSignUp = Utils.castView(view, R.id.textViewSignUp, "field 'textViewSignUp'", TextView.class);
    view7f0a0470 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.textViewSignUp(p0);
      }
    });
    target.textInputLayoutEmail = Utils.findRequiredViewAsType(source, R.id.textInputLayoutEmail, "field 'textInputLayoutEmail'", TextInputLayout.class);
    target.textInputLayoutPassword = Utils.findRequiredViewAsType(source, R.id.textInputLayoutPassword, "field 'textInputLayoutPassword'", TextInputLayout.class);
    target.editTextEmail = Utils.findRequiredViewAsType(source, R.id.editTextEmail, "field 'editTextEmail'", EditText.class);
    target.editTextPassword = Utils.findRequiredViewAsType(source, R.id.editTextPassword, "field 'editTextPassword'", EditText.class);
    target.buttonLoginFacebook = Utils.findRequiredViewAsType(source, R.id.buttonLoginFacebook, "field 'buttonLoginFacebook'", Button.class);
    target.signInButton = Utils.findRequiredViewAsType(source, R.id.sign_in_button, "field 'signInButton'", SignInButton.class);
    view = Utils.findRequiredView(source, R.id.buttonLogin, "field 'buttonLogin' and method 'buttonLogin'");
    target.buttonLogin = Utils.castView(view, R.id.buttonLogin, "field 'buttonLogin'", Button.class);
    view7f0a0080 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.buttonLogin(p0);
      }
    });
    target.layoutProgressRoot = Utils.findRequiredView(source, R.id.layoutProgressRoot, "field 'layoutProgressRoot'");
    target.layoutContainer = Utils.findRequiredView(source, R.id.layoutContainer, "field 'layoutContainer'");
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewForgetPassword = null;
    target.textViewSignUp = null;
    target.textInputLayoutEmail = null;
    target.textInputLayoutPassword = null;
    target.editTextEmail = null;
    target.editTextPassword = null;
    target.buttonLoginFacebook = null;
    target.signInButton = null;
    target.buttonLogin = null;
    target.layoutProgressRoot = null;
    target.layoutContainer = null;

    view7f0a0416.setOnClickListener(null);
    view7f0a0416 = null;
    view7f0a0470.setOnClickListener(null);
    view7f0a0470 = null;
    view7f0a0080.setOnClickListener(null);
    view7f0a0080 = null;
  }
}
