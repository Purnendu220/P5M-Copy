// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.gms.maps.MapView;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClassProfileViewHolder_ViewBinding implements Unbinder {
  private ClassProfileViewHolder target;

  @UiThread
  public ClassProfileViewHolder_ViewBinding(ClassProfileViewHolder target, View source) {
    this.target = target;

    target.imageViewClass = Utils.findRequiredViewAsType(source, R.id.imageViewClass, "field 'imageViewClass'", ImageView.class);
    target.imageViewTrainerProfile = Utils.findRequiredViewAsType(source, R.id.imageViewTrainerProfile, "field 'imageViewTrainerProfile'", ImageView.class);
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
    target.textViewMap = Utils.findRequiredViewAsType(source, R.id.textViewMap, "field 'textViewMap'", TextView.class);
    target.textViewMoreDetails = Utils.findRequiredViewAsType(source, R.id.textViewMoreDetails, "field 'textViewMoreDetails'", TextView.class);
    target.layoutMap = Utils.findRequiredViewAsType(source, R.id.layoutMap, "field 'layoutMap'", LinearLayout.class);
    target.layoutDesc = Utils.findRequiredViewAsType(source, R.id.layoutDesc, "field 'layoutDesc'", LinearLayout.class);
    target.layoutMoreDetails = Utils.findRequiredViewAsType(source, R.id.layoutMoreDetails, "field 'layoutMoreDetails'", LinearLayout.class);
    target.layoutTrainer = Utils.findRequiredViewAsType(source, R.id.layoutTrainer, "field 'layoutTrainer'", LinearLayout.class);
    target.layoutMapClick = Utils.findRequiredView(source, R.id.layoutMapClick, "field 'layoutMapClick'");
    target.layoutLocation = Utils.findRequiredView(source, R.id.layoutLocation, "field 'layoutLocation'");
    target.mapView = Utils.findRequiredViewAsType(source, R.id.mapImageView, "field 'mapView'", MapView.class);
    target.studioRating = Utils.findRequiredViewAsType(source, R.id.studioRating, "field 'studioRating'", RatingBar.class);
    target.textViewRatingCount = Utils.findRequiredViewAsType(source, R.id.textViewRatingCount, "field 'textViewRatingCount'", TextView.class);
    target.textViewReviewCountText = Utils.findRequiredViewAsType(source, R.id.textViewReviewCountText, "field 'textViewReviewCountText'", TextView.class);
    target.linearLayoutStudioRating = Utils.findRequiredViewAsType(source, R.id.linearLayoutStudioRating, "field 'linearLayoutStudioRating'", LinearLayout.class);
    target.layoutSeeAllReview = Utils.findRequiredViewAsType(source, R.id.layoutSeeAllReview, "field 'layoutSeeAllReview'", LinearLayout.class);
    target.linearLayoutClassRating = Utils.findRequiredViewAsType(source, R.id.linearLayoutClassRating, "field 'linearLayoutClassRating'", LinearLayout.class);
    target.textViewClassRating = Utils.findRequiredViewAsType(source, R.id.textViewClassRating, "field 'textViewClassRating'", TextView.class);
    target.relativeLayoutFitnessLevel = Utils.findRequiredViewAsType(source, R.id.relativeLayoutFitnessLevel, "field 'relativeLayoutFitnessLevel'", RelativeLayout.class);
    target.imageViewClassFitnessLevel = Utils.findRequiredViewAsType(source, R.id.imageViewClassFitnessLevel, "field 'imageViewClassFitnessLevel'", ImageView.class);
    target.textViewFitnessLevel = Utils.findRequiredViewAsType(source, R.id.textViewFitnessLevel, "field 'textViewFitnessLevel'", TextView.class);
    target.mapViewLayout = Utils.findRequiredViewAsType(source, R.id.mapView, "field 'mapViewLayout'", FrameLayout.class);
    target.relativeLayoutVideoClass = Utils.findRequiredViewAsType(source, R.id.relativeLayoutVideoClass, "field 'relativeLayoutVideoClass'", RelativeLayout.class);
    target.layoutSeats = Utils.findRequiredViewAsType(source, R.id.layoutSeats, "field 'layoutSeats'", RelativeLayout.class);
    target.layoutChannelName = Utils.findRequiredViewAsType(source, R.id.layoutChannelName, "field 'layoutChannelName'", RelativeLayout.class);
    target.textViewChannelNAme = Utils.findRequiredViewAsType(source, R.id.textViewChannelNAme, "field 'textViewChannelNAme'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ClassProfileViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewClass = null;
    target.imageViewTrainerProfile = null;
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
    target.textViewMap = null;
    target.textViewMoreDetails = null;
    target.layoutMap = null;
    target.layoutDesc = null;
    target.layoutMoreDetails = null;
    target.layoutTrainer = null;
    target.layoutMapClick = null;
    target.layoutLocation = null;
    target.mapView = null;
    target.studioRating = null;
    target.textViewRatingCount = null;
    target.textViewReviewCountText = null;
    target.linearLayoutStudioRating = null;
    target.layoutSeeAllReview = null;
    target.linearLayoutClassRating = null;
    target.textViewClassRating = null;
    target.relativeLayoutFitnessLevel = null;
    target.imageViewClassFitnessLevel = null;
    target.textViewFitnessLevel = null;
    target.mapViewLayout = null;
    target.relativeLayoutVideoClass = null;
    target.layoutSeats = null;
    target.layoutChannelName = null;
    target.textViewChannelNAme = null;
  }
}
