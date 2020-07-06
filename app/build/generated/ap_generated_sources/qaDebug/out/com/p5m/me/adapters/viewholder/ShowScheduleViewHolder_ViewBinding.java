// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.cardview.widget.CardView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShowScheduleViewHolder_ViewBinding implements Unbinder {
  private ShowScheduleViewHolder target;

  @UiThread
  public ShowScheduleViewHolder_ViewBinding(ShowScheduleViewHolder target, View source) {
    this.target = target;

    target.textViewClassName = Utils.findRequiredViewAsType(source, R.id.textViewClassName, "field 'textViewClassName'", TextView.class);
    target.textViewTrainerName = Utils.findRequiredViewAsType(source, R.id.textViewTrainerName, "field 'textViewTrainerName'", TextView.class);
    target.textViewClassTime = Utils.findRequiredViewAsType(source, R.id.textViewClassTime, "field 'textViewClassTime'", TextView.class);
    target.layoutScheduler = Utils.findRequiredViewAsType(source, R.id.layoutScheduler, "field 'layoutScheduler'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShowScheduleViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewClassName = null;
    target.textViewTrainerName = null;
    target.textViewClassTime = null;
    target.layoutScheduler = null;
  }
}
