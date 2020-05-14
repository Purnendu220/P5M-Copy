// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClassMiniDetailViewHolder_ViewBinding implements Unbinder {
  private ClassMiniDetailViewHolder target;

  @UiThread
  public ClassMiniDetailViewHolder_ViewBinding(ClassMiniDetailViewHolder target, View source) {
    this.target = target;

    target.imageViewClass = Utils.findRequiredViewAsType(source, R.id.imageViewClass, "field 'imageViewClass'", ImageView.class);
    target.imageViewTrainerProfile = Utils.findRequiredViewAsType(source, R.id.imageViewTrainerProfile, "field 'imageViewTrainerProfile'", ImageView.class);
    target.imageViewOptions1 = Utils.findRequiredViewAsType(source, R.id.imageViewOptions1, "field 'imageViewOptions1'", ImageView.class);
    target.imageViewOptions2 = Utils.findRequiredViewAsType(source, R.id.imageViewOptions2, "field 'imageViewOptions2'", ImageView.class);
    target.imageViewClassGender = Utils.findRequiredViewAsType(source, R.id.imageViewClassGender, "field 'imageViewClassGender'", ImageView.class);
    target.imageViewInviteFriend = Utils.findRequiredViewAsType(source, R.id.imageViewInviteFriend, "field 'imageViewInviteFriend'", ImageView.class);
    target.trainerImage = Utils.findRequiredView(source, R.id.trainerImage, "field 'trainerImage'");
    target.layoutSpecial = Utils.findRequiredView(source, R.id.layoutSpecial, "field 'layoutSpecial'");
    target.buttonJoin = Utils.findRequiredViewAsType(source, R.id.buttonJoin, "field 'buttonJoin'", Button.class);
    target.imageViewChat = Utils.findRequiredViewAsType(source, R.id.imageViewChat, "field 'imageViewChat'", ImageView.class);
    target.imageViewVideoClass = Utils.findRequiredViewAsType(source, R.id.imageViewVideoClass, "field 'imageViewVideoClass'", ImageView.class);
    target.layoutGender = Utils.findRequiredView(source, R.id.layoutGender, "field 'layoutGender'");
    target.textViewClassGender = Utils.findRequiredViewAsType(source, R.id.textViewClassGender, "field 'textViewClassGender'", TextView.class);
    target.textViewSpecialClass = Utils.findRequiredViewAsType(source, R.id.textViewSpecialClass, "field 'textViewSpecialClass'", TextView.class);
    target.textViewClassName = Utils.findRequiredViewAsType(source, R.id.textViewClassName, "field 'textViewClassName'", TextView.class);
    target.textViewClassCategory = Utils.findRequiredViewAsType(source, R.id.textViewClassCategory, "field 'textViewClassCategory'", TextView.class);
    target.textViewClassDate = Utils.findRequiredViewAsType(source, R.id.textViewClassDate, "field 'textViewClassDate'", TextView.class);
    target.textViewTime = Utils.findRequiredViewAsType(source, R.id.textViewTime, "field 'textViewTime'", TextView.class);
    target.textViewLocation = Utils.findRequiredViewAsType(source, R.id.textViewLocation, "field 'textViewLocation'", TextView.class);
    target.textViewTrainerName = Utils.findRequiredViewAsType(source, R.id.textViewTrainerName, "field 'textViewTrainerName'", TextView.class);
    target.buttonEditRating = Utils.findRequiredViewAsType(source, R.id.buttonEditRating, "field 'buttonEditRating'", Button.class);
    target.buttonClassRating = Utils.findRequiredViewAsType(source, R.id.buttonClassRating, "field 'buttonClassRating'", Button.class);
    target.relativeLayoutFitnessLevel = Utils.findRequiredViewAsType(source, R.id.relativeLayoutFitnessLevel, "field 'relativeLayoutFitnessLevel'", RelativeLayout.class);
    target.imageViewClassFitnessLevel = Utils.findRequiredViewAsType(source, R.id.imageViewClassFitnessLevel, "field 'imageViewClassFitnessLevel'", ImageView.class);
    target.textViewFitnessLevel = Utils.findRequiredViewAsType(source, R.id.textViewFitnessLevel, "field 'textViewFitnessLevel'", TextView.class);
    target.classBookedWithFriend = Utils.findRequiredViewAsType(source, R.id.classBookedWithFriend, "field 'classBookedWithFriend'", RelativeLayout.class);
    target.layoutChannelName = Utils.findRequiredViewAsType(source, R.id.layoutChannelName, "field 'layoutChannelName'", RelativeLayout.class);
    target.textViewChannelNAme = Utils.findRequiredViewAsType(source, R.id.textViewChannelNAme, "field 'textViewChannelNAme'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ClassMiniDetailViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewClass = null;
    target.imageViewTrainerProfile = null;
    target.imageViewOptions1 = null;
    target.imageViewOptions2 = null;
    target.imageViewClassGender = null;
    target.imageViewInviteFriend = null;
    target.trainerImage = null;
    target.layoutSpecial = null;
    target.buttonJoin = null;
    target.imageViewChat = null;
    target.imageViewVideoClass = null;
    target.layoutGender = null;
    target.textViewClassGender = null;
    target.textViewSpecialClass = null;
    target.textViewClassName = null;
    target.textViewClassCategory = null;
    target.textViewClassDate = null;
    target.textViewTime = null;
    target.textViewLocation = null;
    target.textViewTrainerName = null;
    target.buttonEditRating = null;
    target.buttonClassRating = null;
    target.relativeLayoutFitnessLevel = null;
    target.imageViewClassFitnessLevel = null;
    target.textViewFitnessLevel = null;
    target.classBookedWithFriend = null;
    target.layoutChannelName = null;
    target.textViewChannelNAme = null;
  }
}
