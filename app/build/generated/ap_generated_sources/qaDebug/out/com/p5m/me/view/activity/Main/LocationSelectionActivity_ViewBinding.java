// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LocationSelectionActivity_ViewBinding implements Unbinder {
  private LocationSelectionActivity target;

  @UiThread
  public LocationSelectionActivity_ViewBinding(LocationSelectionActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LocationSelectionActivity_ViewBinding(LocationSelectionActivity target, View source) {
    this.target = target;

    target.spinnerCity = Utils.findRequiredViewAsType(source, R.id.spinnerCity, "field 'spinnerCity'", Spinner.class);
    target.buttonMale = Utils.findRequiredViewAsType(source, R.id.buttonMale, "field 'buttonMale'", Button.class);
    target.buttonFemale = Utils.findRequiredViewAsType(source, R.id.buttonFemale, "field 'buttonFemale'", Button.class);
    target.buttonNext = Utils.findRequiredViewAsType(source, R.id.buttonNext, "field 'buttonNext'", Button.class);
    target.textViewCountryName = Utils.findRequiredViewAsType(source, R.id.textViewCountryName, "field 'textViewCountryName'", EditText.class);
    target.textInputLayoutCity = Utils.findRequiredViewAsType(source, R.id.textInputLayoutCity, "field 'textInputLayoutCity'", TextInputLayout.class);
    target.textViewLogin = Utils.findRequiredViewAsType(source, R.id.textViewLogin, "field 'textViewLogin'", TextView.class);
    target.textViewHeader = Utils.findRequiredViewAsType(source, R.id.textViewHeader, "field 'textViewHeader'", TextView.class);
    target.layoutGender = Utils.findRequiredViewAsType(source, R.id.layoutGender, "field 'layoutGender'", LinearLayout.class);
    target.view = Utils.findRequiredView(source, R.id.view, "field 'view'");
  }

  @Override
  @CallSuper
  public void unbind() {
    LocationSelectionActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.spinnerCity = null;
    target.buttonMale = null;
    target.buttonFemale = null;
    target.buttonNext = null;
    target.textViewCountryName = null;
    target.textInputLayoutCity = null;
    target.textViewLogin = null;
    target.textViewHeader = null;
    target.layoutGender = null;
    target.view = null;
  }
}
