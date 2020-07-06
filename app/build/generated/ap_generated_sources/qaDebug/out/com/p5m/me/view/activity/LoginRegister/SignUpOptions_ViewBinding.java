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
import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SignUpOptions_ViewBinding implements Unbinder {
  private SignUpOptions target;

  private View view7f0a0429;

  private View view7f0a0080;

  @UiThread
  public SignUpOptions_ViewBinding(SignUpOptions target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignUpOptions_ViewBinding(final SignUpOptions target, View source) {
    this.target = target;

    View view;
    target.textViewBottom = Utils.findRequiredViewAsType(source, R.id.textViewBottom, "field 'textViewBottom'", TextView.class);
    target.buttonLoginFacebook = Utils.findRequiredViewAsType(source, R.id.buttonLoginFacebook, "field 'buttonLoginFacebook'", Button.class);
    target.layoutProgress = Utils.findRequiredView(source, R.id.layoutProgress, "field 'layoutProgress'");
    view = Utils.findRequiredView(source, R.id.textViewLogin, "field 'textViewLogin' and method 'textViewLogin'");
    target.textViewLogin = Utils.castView(view, R.id.textViewLogin, "field 'textViewLogin'", TextView.class);
    view7f0a0429 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.textViewLogin(p0);
      }
    });
    target.textViewGenderError = Utils.findRequiredViewAsType(source, R.id.textViewGenderError, "field 'textViewGenderError'", TextView.class);
    target.textInputLayoutName = Utils.findRequiredViewAsType(source, R.id.textInputLayoutName, "field 'textInputLayoutName'", TextInputLayout.class);
    target.textInputLayoutLastName = Utils.findRequiredViewAsType(source, R.id.textInputLayoutLastName, "field 'textInputLayoutLastName'", TextInputLayout.class);
    target.textInputLayoutEmail = Utils.findRequiredViewAsType(source, R.id.textInputLayoutEmail, "field 'textInputLayoutEmail'", TextInputLayout.class);
    target.textInputLayoutPass = Utils.findRequiredViewAsType(source, R.id.textInputLayoutPass, "field 'textInputLayoutPass'", TextInputLayout.class);
    target.textInputLayoutConfirmPass = Utils.findRequiredViewAsType(source, R.id.textInputLayoutConfirmPass, "field 'textInputLayoutConfirmPass'", TextInputLayout.class);
    target.editTextConfirmPass = Utils.findRequiredViewAsType(source, R.id.editTextConfirmPass, "field 'editTextConfirmPass'", EditText.class);
    target.editTextPass = Utils.findRequiredViewAsType(source, R.id.editTextPass, "field 'editTextPass'", EditText.class);
    target.editTextEmail = Utils.findRequiredViewAsType(source, R.id.editTextEmail, "field 'editTextEmail'", EditText.class);
    target.editTextName = Utils.findRequiredViewAsType(source, R.id.editTextName, "field 'editTextName'", EditText.class);
    target.editTextLastName = Utils.findRequiredViewAsType(source, R.id.editTextLastName, "field 'editTextLastName'", EditText.class);
    target.buttonMale = Utils.findRequiredViewAsType(source, R.id.buttonMale, "field 'buttonMale'", Button.class);
    target.buttonFemale = Utils.findRequiredViewAsType(source, R.id.buttonFemale, "field 'buttonFemale'", Button.class);
    target.buttonNext = Utils.findRequiredViewAsType(source, R.id.buttonNext, "field 'buttonNext'", Button.class);
    view = Utils.findRequiredView(source, R.id.buttonLogin, "method 'buttonLogin'");
    view7f0a0080 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.buttonLogin(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SignUpOptions target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewBottom = null;
    target.buttonLoginFacebook = null;
    target.layoutProgress = null;
    target.textViewLogin = null;
    target.textViewGenderError = null;
    target.textInputLayoutName = null;
    target.textInputLayoutLastName = null;
    target.textInputLayoutEmail = null;
    target.textInputLayoutPass = null;
    target.textInputLayoutConfirmPass = null;
    target.editTextConfirmPass = null;
    target.editTextPass = null;
    target.editTextEmail = null;
    target.editTextName = null;
    target.editTextLastName = null;
    target.buttonMale = null;
    target.buttonFemale = null;
    target.buttonNext = null;

    view7f0a0429.setOnClickListener(null);
    view7f0a0429 = null;
    view7f0a0080.setOnClickListener(null);
    view7f0a0080 = null;
  }
}
