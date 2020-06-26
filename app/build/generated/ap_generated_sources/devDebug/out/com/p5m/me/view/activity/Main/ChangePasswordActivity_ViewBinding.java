// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChangePasswordActivity_ViewBinding implements Unbinder {
  private ChangePasswordActivity target;

  private View view7f0a0193;

  private View view7f0a017f;

  @UiThread
  public ChangePasswordActivity_ViewBinding(ChangePasswordActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChangePasswordActivity_ViewBinding(final ChangePasswordActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.imageViewDone, "field 'imageViewDone' and method 'imageViewDone'");
    target.imageViewDone = Utils.castView(view, R.id.imageViewDone, "field 'imageViewDone'", ImageView.class);
    view7f0a0193 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imageViewDone(p0);
      }
    });
    target.progressBarDone = Utils.findRequiredViewAsType(source, R.id.progressBarDone, "field 'progressBarDone'", ProgressBar.class);
    target.textInputLayoutCurrPass = Utils.findRequiredViewAsType(source, R.id.textInputLayoutCurrPass, "field 'textInputLayoutCurrPass'", TextInputLayout.class);
    target.textInputLayoutNewPass = Utils.findRequiredViewAsType(source, R.id.textInputLayoutNewPass, "field 'textInputLayoutNewPass'", TextInputLayout.class);
    target.textInputConfirmPass = Utils.findRequiredViewAsType(source, R.id.textInputConfirmPass, "field 'textInputConfirmPass'", TextInputLayout.class);
    target.editTextConfirmPass = Utils.findRequiredViewAsType(source, R.id.editTextConfirmPass, "field 'editTextConfirmPass'", EditText.class);
    target.editTextNewPass = Utils.findRequiredViewAsType(source, R.id.editTextNewPass, "field 'editTextNewPass'", EditText.class);
    target.editTextCurrPass = Utils.findRequiredViewAsType(source, R.id.editTextCurrPass, "field 'editTextCurrPass'", EditText.class);
    view = Utils.findRequiredView(source, R.id.imageViewBack, "method 'imageViewBack'");
    view7f0a017f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imageViewBack(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ChangePasswordActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewDone = null;
    target.progressBarDone = null;
    target.textInputLayoutCurrPass = null;
    target.textInputLayoutNewPass = null;
    target.textInputConfirmPass = null;
    target.editTextConfirmPass = null;
    target.editTextNewPass = null;
    target.editTextCurrPass = null;

    view7f0a0193.setOnClickListener(null);
    view7f0a0193 = null;
    view7f0a017f.setOnClickListener(null);
    view7f0a017f = null;
  }
}
