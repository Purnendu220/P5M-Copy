// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ContactActivity_ViewBinding implements Unbinder {
  private ContactActivity target;

  @UiThread
  public ContactActivity_ViewBinding(ContactActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ContactActivity_ViewBinding(ContactActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.radioGroupSender = Utils.findRequiredViewAsType(source, R.id.radioGroupSender, "field 'radioGroupSender'", RadioGroup.class);
    target.radioButtonSendGym = Utils.findRequiredViewAsType(source, R.id.radioButtonSendGym, "field 'radioButtonSendGym'", RadioButton.class);
    target.radioButtonSendTrainer = Utils.findRequiredViewAsType(source, R.id.radioButtonSendTrainer, "field 'radioButtonSendTrainer'", RadioButton.class);
    target.etMessage = Utils.findRequiredViewAsType(source, R.id.etMessage, "field 'etMessage'", EditText.class);
    target.imageViewClass = Utils.findRequiredViewAsType(source, R.id.imageViewClass, "field 'imageViewClass'", ImageView.class);
    target.buttonSendMessage = Utils.findRequiredViewAsType(source, R.id.buttonSendMessage, "field 'buttonSendMessage'", Button.class);
    target.textViewClassName = Utils.findRequiredViewAsType(source, R.id.textViewClassName, "field 'textViewClassName'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ContactActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.radioGroupSender = null;
    target.radioButtonSendGym = null;
    target.radioButtonSendTrainer = null;
    target.etMessage = null;
    target.imageViewClass = null;
    target.buttonSendMessage = null;
    target.textViewClassName = null;
  }
}
