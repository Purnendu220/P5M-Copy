// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WorkoutViewHolder_ViewBinding implements Unbinder {
  private WorkoutViewHolder target;

  @UiThread
  public WorkoutViewHolder_ViewBinding(WorkoutViewHolder target, View source) {
    this.target = target;

    target.imageViewClass = Utils.findRequiredViewAsType(source, R.id.imageViewClass, "field 'imageViewClass'", ImageView.class);
    target.textViewGymName = Utils.findRequiredViewAsType(source, R.id.textViewGymName, "field 'textViewGymName'", TextView.class);
    target.trainerName = Utils.findRequiredViewAsType(source, R.id.trainerName, "field 'trainerName'", TextView.class);
    target.textViewPriceModel = Utils.findRequiredViewAsType(source, R.id.textViewPriceModel, "field 'textViewPriceModel'", TextView.class);
    target.textViewWorkoutType = Utils.findRequiredViewAsType(source, R.id.textViewWorkoutType, "field 'textViewWorkoutType'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WorkoutViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewClass = null;
    target.textViewGymName = null;
    target.trainerName = null;
    target.textViewPriceModel = null;
    target.textViewWorkoutType = null;
  }
}
