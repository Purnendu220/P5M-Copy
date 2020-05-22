// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FAQViewHolder_ViewBinding implements Unbinder {
  private FAQViewHolder target;

  @UiThread
  public FAQViewHolder_ViewBinding(FAQViewHolder target, View source) {
    this.target = target;

    target.textViewQuestion = Utils.findRequiredViewAsType(source, R.id.textViewQuestions, "field 'textViewQuestion'", TextView.class);
    target.textViewAnswer = Utils.findRequiredViewAsType(source, R.id.textViewAnswer, "field 'textViewAnswer'", TextView.class);
    target.view = Utils.findRequiredView(source, R.id.view, "field 'view'");
  }

  @Override
  @CallSuper
  public void unbind() {
    FAQViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewQuestion = null;
    target.textViewAnswer = null;
    target.view = null;
  }
}
