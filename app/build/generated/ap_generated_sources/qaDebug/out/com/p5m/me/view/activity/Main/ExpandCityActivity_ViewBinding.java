// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ExpandCityActivity_ViewBinding implements Unbinder {
  private ExpandCityActivity target;

  @UiThread
  public ExpandCityActivity_ViewBinding(ExpandCityActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ExpandCityActivity_ViewBinding(ExpandCityActivity target, View source) {
    this.target = target;

    target.buttonSubmit = Utils.findRequiredViewAsType(source, R.id.buttonSubmit, "field 'buttonSubmit'", Button.class);
    target.textViewLogin = Utils.findRequiredViewAsType(source, R.id.textViewLogin, "field 'textViewLogin'", TextView.class);
    target.textViewHeader = Utils.findRequiredViewAsType(source, R.id.textViewHeader, "field 'textViewHeader'", TextView.class);
    target.editTextEmail = Utils.findRequiredViewAsType(source, R.id.editTextEmail, "field 'editTextEmail'", EditText.class);
    target.imageViewBack = Utils.findRequiredViewAsType(source, R.id.imageViewBack, "field 'imageViewBack'", ImageView.class);
    target.textViewSkip = Utils.findRequiredViewAsType(source, R.id.textViewSkip, "field 'textViewSkip'", TextView.class);
    target.textInputLayoutEmail = Utils.findRequiredViewAsType(source, R.id.textInputLayoutEmail, "field 'textInputLayoutEmail'", TextInputLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ExpandCityActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.buttonSubmit = null;
    target.textViewLogin = null;
    target.textViewHeader = null;
    target.editTextEmail = null;
    target.imageViewBack = null;
    target.textViewSkip = null;
    target.textInputLayoutEmail = null;
  }
}
