// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClassViewHolder_ViewBinding implements Unbinder {
  private ClassViewHolder target;

  @UiThread
  public ClassViewHolder_ViewBinding(ClassViewHolder target, View source) {
    this.target = target;

    target.imageViewClass = Utils.findRequiredViewAsType(source, R.id.imageViewClass, "field 'imageViewClass'", ImageView.class);
    target.imageViewTrainerProfile = Utils.findRequiredViewAsType(source, R.id.imageViewTrainerProfile, "field 'imageViewTrainerProfile'", ImageView.class);
    target.imageViewOptions = Utils.findRequiredViewAsType(source, R.id.imageViewOptions, "field 'imageViewOptions'", ImageView.class);
    target.buttonJoin = Utils.findRequiredViewAsType(source, R.id.buttonJoin, "field 'buttonJoin'", Button.class);
    target.textViewClassName = Utils.findRequiredViewAsType(source, R.id.textViewClassName, "field 'textViewClassName'", TextView.class);
    target.textViewClassCategory = Utils.findRequiredViewAsType(source, R.id.textViewClassCategory, "field 'textViewClassCategory'", TextView.class);
    target.textViewClassDate = Utils.findRequiredViewAsType(source, R.id.textViewClassDate, "field 'textViewClassDate'", TextView.class);
    target.textViewAvailable = Utils.findRequiredViewAsType(source, R.id.textViewAvailable, "field 'textViewAvailable'", TextView.class);
    target.textViewInfo = Utils.findRequiredViewAsType(source, R.id.textViewInfo, "field 'textViewInfo'", TextView.class);
    target.textViewTime = Utils.findRequiredViewAsType(source, R.id.textViewTime, "field 'textViewTime'", TextView.class);
    target.textViewLocation = Utils.findRequiredViewAsType(source, R.id.textViewLocation, "field 'textViewLocation'", TextView.class);
    target.textViewGender = Utils.findRequiredViewAsType(source, R.id.textViewGender, "field 'textViewGender'", TextView.class);
    target.textViewTrainerName = Utils.findRequiredViewAsType(source, R.id.textViewTrainerName, "field 'textViewTrainerName'", TextView.class);
    target.textViewSpecialClass = Utils.findRequiredViewAsType(source, R.id.textViewSpecialClass, "field 'textViewSpecialClass'", TextView.class);
    target.layoutTrainer = Utils.findRequiredViewAsType(source, R.id.layoutTrainer, "field 'layoutTrainer'", LinearLayout.class);
    target.linearLayoutClassRating = Utils.findRequiredViewAsType(source, R.id.linearLayoutClassRating, "field 'linearLayoutClassRating'", LinearLayout.class);
    target.textViewClassRating = Utils.findRequiredViewAsType(source, R.id.textViewClassRating, "field 'textViewClassRating'", TextView.class);
    target.layoutLocation = Utils.findRequiredView(source, R.id.layoutLocation, "field 'layoutLocation'");
    target.layoutFitnessLevel = Utils.findRequiredViewAsType(source, R.id.layoutFitnessLevel, "field 'layoutFitnessLevel'", LinearLayout.class);
    target.imageViewClassFitnessLevel = Utils.findRequiredViewAsType(source, R.id.imageViewClassFitnessLevel, "field 'imageViewClassFitnessLevel'", ImageView.class);
    target.textViewFitnessLevel = Utils.findRequiredViewAsType(source, R.id.textViewFitnessLevel, "field 'textViewFitnessLevel'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ClassViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewClass = null;
    target.imageViewTrainerProfile = null;
    target.imageViewOptions = null;
    target.buttonJoin = null;
    target.textViewClassName = null;
    target.textViewClassCategory = null;
    target.textViewClassDate = null;
    target.textViewAvailable = null;
    target.textViewInfo = null;
    target.textViewTime = null;
    target.textViewLocation = null;
    target.textViewGender = null;
    target.textViewTrainerName = null;
    target.textViewSpecialClass = null;
    target.layoutTrainer = null;
    target.linearLayoutClassRating = null;
    target.textViewClassRating = null;
    target.layoutLocation = null;
    target.layoutFitnessLevel = null;
    target.imageViewClassFitnessLevel = null;
    target.textViewFitnessLevel = null;
  }
}
