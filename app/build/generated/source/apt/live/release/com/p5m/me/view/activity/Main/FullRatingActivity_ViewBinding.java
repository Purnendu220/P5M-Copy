// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FullRatingActivity_ViewBinding implements Unbinder {
  private FullRatingActivity target;

  @UiThread
  public FullRatingActivity_ViewBinding(FullRatingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FullRatingActivity_ViewBinding(FullRatingActivity target, View source) {
    this.target = target;

    target.textViewHowWasExperiance = Utils.findRequiredViewAsType(source, R.id.textViewHowWasExperiance, "field 'textViewHowWasExperiance'", TextView.class);
    target.textViewWhatCouldBeImproved = Utils.findRequiredViewAsType(source, R.id.textViewWhatCouldBeImproved, "field 'textViewWhatCouldBeImproved'", TextView.class);
    target.flexBoxLayoutOptions = Utils.findRequiredViewAsType(source, R.id.flexBoxLayoutOptions, "field 'flexBoxLayoutOptions'", FlexboxLayout.class);
    target.textInputLayoutComment = Utils.findRequiredViewAsType(source, R.id.textInputLayoutComment, "field 'textInputLayoutComment'", TextInputLayout.class);
    target.editTextComment = Utils.findRequiredViewAsType(source, R.id.editTextComment, "field 'editTextComment'", EditText.class);
    target.imageViewChooseImage = Utils.findRequiredViewAsType(source, R.id.imageViewChooseImage, "field 'imageViewChooseImage'", ImageView.class);
    target.ratingStar1 = Utils.findRequiredViewAsType(source, R.id.ratingStar1, "field 'ratingStar1'", ImageView.class);
    target.ratingStar2 = Utils.findRequiredViewAsType(source, R.id.ratingStar2, "field 'ratingStar2'", ImageView.class);
    target.ratingStar3 = Utils.findRequiredViewAsType(source, R.id.ratingStar3, "field 'ratingStar3'", ImageView.class);
    target.ratingStar4 = Utils.findRequiredViewAsType(source, R.id.ratingStar4, "field 'ratingStar4'", ImageView.class);
    target.ratingStar5 = Utils.findRequiredViewAsType(source, R.id.ratingStar5, "field 'ratingStar5'", ImageView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.selectedImageRecyclerView = Utils.findRequiredViewAsType(source, R.id.selectedImageRecyclerView, "field 'selectedImageRecyclerView'", RecyclerView.class);
    target.rateClasses = Utils.findRequiredViewAsType(source, R.id.rateClasses, "field 'rateClasses'", Button.class);
    target.progressBarRating = Utils.findRequiredViewAsType(source, R.id.progressBarRating, "field 'progressBarRating'", ProgressBar.class);
    target.parentLayout = Utils.findRequiredViewAsType(source, R.id.parentLayout, "field 'parentLayout'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FullRatingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewHowWasExperiance = null;
    target.textViewWhatCouldBeImproved = null;
    target.flexBoxLayoutOptions = null;
    target.textInputLayoutComment = null;
    target.editTextComment = null;
    target.imageViewChooseImage = null;
    target.ratingStar1 = null;
    target.ratingStar2 = null;
    target.ratingStar3 = null;
    target.ratingStar4 = null;
    target.ratingStar5 = null;
    target.toolbar = null;
    target.selectedImageRecyclerView = null;
    target.rateClasses = null;
    target.progressBarRating = null;
    target.parentLayout = null;
  }
}
