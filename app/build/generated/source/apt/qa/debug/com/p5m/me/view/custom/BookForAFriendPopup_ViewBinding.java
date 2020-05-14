// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.custom;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BookForAFriendPopup_ViewBinding implements Unbinder {
  private BookForAFriendPopup target;

  @UiThread
  public BookForAFriendPopup_ViewBinding(BookForAFriendPopup target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public BookForAFriendPopup_ViewBinding(BookForAFriendPopup target, View source) {
    this.target = target;

    target.textInputLayoutFriendsName = Utils.findRequiredViewAsType(source, R.id.textInputLayoutFriendsName, "field 'textInputLayoutFriendsName'", TextInputLayout.class);
    target.textViewWarningRefund = Utils.findRequiredViewAsType(source, R.id.textViewWarningRefund, "field 'textViewWarningRefund'", TextView.class);
    target.editTextFriendsName = Utils.findRequiredViewAsType(source, R.id.editTextFriendsName, "field 'editTextFriendsName'", EditText.class);
    target.textInputLayoutFriendsEmail = Utils.findRequiredViewAsType(source, R.id.textInputLayoutFriendsEmail, "field 'textInputLayoutFriendsEmail'", TextInputLayout.class);
    target.editTextFriendsEmail = Utils.findRequiredViewAsType(source, R.id.editTextFriendsEmail, "field 'editTextFriendsEmail'", EditText.class);
    target.textViewBookWithFriend = Utils.findRequiredViewAsType(source, R.id.textViewBookWithFriend, "field 'textViewBookWithFriend'", TextView.class);
    target.buttonMale = Utils.findRequiredViewAsType(source, R.id.buttonMale, "field 'buttonMale'", Button.class);
    target.buttonFemale = Utils.findRequiredViewAsType(source, R.id.buttonFemale, "field 'buttonFemale'", Button.class);
    target.textViewGenderError = Utils.findRequiredViewAsType(source, R.id.textViewGenderError, "field 'textViewGenderError'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BookForAFriendPopup target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textInputLayoutFriendsName = null;
    target.textViewWarningRefund = null;
    target.editTextFriendsName = null;
    target.textInputLayoutFriendsEmail = null;
    target.editTextFriendsEmail = null;
    target.textViewBookWithFriend = null;
    target.buttonMale = null;
    target.buttonFemale = null;
    target.textViewGenderError = null;
  }
}
