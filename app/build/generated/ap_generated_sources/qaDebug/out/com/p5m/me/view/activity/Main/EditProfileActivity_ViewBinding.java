// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditProfileActivity_ViewBinding implements Unbinder {
  private EditProfileActivity target;

  private View view7f0a017f;

  private View view7f0a0193;

  @UiThread
  public EditProfileActivity_ViewBinding(EditProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EditProfileActivity_ViewBinding(final EditProfileActivity target, View source) {
    this.target = target;

    View view;
    target.editTextNameFirst = Utils.findRequiredViewAsType(source, R.id.editTextNameFirst, "field 'editTextNameFirst'", EditText.class);
    target.editTextNameLast = Utils.findRequiredViewAsType(source, R.id.editTextNameLast, "field 'editTextNameLast'", EditText.class);
    target.editTextEmail = Utils.findRequiredViewAsType(source, R.id.editTextEmail, "field 'editTextEmail'", EditText.class);
    target.editTextMobile = Utils.findRequiredViewAsType(source, R.id.editTextMobile, "field 'editTextMobile'", EditText.class);
    target.buttonMale = Utils.findRequiredViewAsType(source, R.id.buttonMale, "field 'buttonMale'", Button.class);
    target.buttonFemale = Utils.findRequiredViewAsType(source, R.id.buttonFemale, "field 'buttonFemale'", Button.class);
    target.layoutChooseFocus = Utils.findRequiredView(source, R.id.layoutChooseFocus, "field 'layoutChooseFocus'");
    target.layoutChangePass = Utils.findRequiredView(source, R.id.layoutChangePass, "field 'layoutChangePass'");
    target.layoutDob = Utils.findRequiredView(source, R.id.layoutDob, "field 'layoutDob'");
    target.textViewLocation = Utils.findRequiredViewAsType(source, R.id.textViewLocation, "field 'textViewLocation'", TextView.class);
    target.textViewNationality = Utils.findRequiredViewAsType(source, R.id.textViewNationality, "field 'textViewNationality'", TextView.class);
    target.textViewDob = Utils.findRequiredViewAsType(source, R.id.textViewDob, "field 'textViewDob'", TextView.class);
    target.imageViewProfile = Utils.findRequiredViewAsType(source, R.id.imageViewProfile, "field 'imageViewProfile'", ImageView.class);
    target.imageViewDob = Utils.findRequiredViewAsType(source, R.id.imageViewDob, "field 'imageViewDob'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.imageViewBack, "field 'imageViewBack' and method 'imageViewBack'");
    target.imageViewBack = Utils.castView(view, R.id.imageViewBack, "field 'imageViewBack'", ImageView.class);
    view7f0a017f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imageViewBack(p0);
      }
    });
    target.imageViewCamera = Utils.findRequiredViewAsType(source, R.id.imageViewCamera, "field 'imageViewCamera'", ImageView.class);
    target.progressBar = Utils.findRequiredView(source, R.id.progressBar, "field 'progressBar'");
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
  }

  @Override
  @CallSuper
  public void unbind() {
    EditProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.editTextNameFirst = null;
    target.editTextNameLast = null;
    target.editTextEmail = null;
    target.editTextMobile = null;
    target.buttonMale = null;
    target.buttonFemale = null;
    target.layoutChooseFocus = null;
    target.layoutChangePass = null;
    target.layoutDob = null;
    target.textViewLocation = null;
    target.textViewNationality = null;
    target.textViewDob = null;
    target.imageViewProfile = null;
    target.imageViewDob = null;
    target.imageViewBack = null;
    target.imageViewCamera = null;
    target.progressBar = null;
    target.imageViewDone = null;
    target.progressBarDone = null;

    view7f0a017f.setOnClickListener(null);
    view7f0a017f = null;
    view7f0a0193.setOnClickListener(null);
    view7f0a0193 = null;
  }
}
