// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClassProfileActivityNew_ViewBinding implements Unbinder {
  private ClassProfileActivityNew target;

  private View view7f0a03d9;

  private View view7f0a03da;

  @UiThread
  public ClassProfileActivityNew_ViewBinding(ClassProfileActivityNew target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ClassProfileActivityNew_ViewBinding(final ClassProfileActivityNew target, View source) {
    this.target = target;

    View view;
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.appBarLayout, "field 'appBarLayout'", AppBarLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.layoutButton = Utils.findRequiredView(source, R.id.layoutButton, "field 'layoutButton'");
    view = Utils.findRequiredView(source, R.id.textViewBook, "field 'textViewBook' and method 'textViewBook'");
    target.textViewBook = Utils.castView(view, R.id.textViewBook, "field 'textViewBook'", TextView.class);
    view7f0a03d9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.textViewBook();
      }
    });
    view = Utils.findRequiredView(source, R.id.textViewBookWithFriend, "field 'textViewBookWithFriend' and method 'textViewBookWithFriend'");
    target.textViewBookWithFriend = Utils.castView(view, R.id.textViewBookWithFriend, "field 'textViewBookWithFriend'", TextView.class);
    view7f0a03da = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.textViewBookWithFriend();
      }
    });
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipeRefreshLayout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.imageViewClass = Utils.findRequiredViewAsType(source, R.id.imageViewClass, "field 'imageViewClass'", ImageView.class);
    target.imageViewTrainerProfile = Utils.findRequiredViewAsType(source, R.id.imageViewTrainerProfile, "field 'imageViewTrainerProfile'", ImageView.class);
    target.imageViewMap = Utils.findRequiredViewAsType(source, R.id.imageViewMap, "field 'imageViewMap'", ImageView.class);
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
  }

  @Override
  @CallSuper
  public void unbind() {
    ClassProfileActivityNew target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.appBarLayout = null;
    target.toolbar = null;
    target.layoutButton = null;
    target.textViewBook = null;
    target.textViewBookWithFriend = null;
    target.swipeRefreshLayout = null;
    target.imageViewClass = null;
    target.imageViewTrainerProfile = null;
    target.imageViewMap = null;
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

    view7f0a03d9.setOnClickListener(null);
    view7f0a03d9 = null;
    view7f0a03da.setOnClickListener(null);
    view7f0a03da = null;
  }
}
