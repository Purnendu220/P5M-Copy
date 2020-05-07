// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RatingViewHolder_ViewBinding implements Unbinder {
  private RatingViewHolder target;

  @UiThread
  public RatingViewHolder_ViewBinding(RatingViewHolder target, View source) {
    this.target = target;

    target.imageViewUserProfile = Utils.findRequiredViewAsType(source, R.id.imageViewUserProfile, "field 'imageViewUserProfile'", ImageView.class);
    target.textViewUsername = Utils.findRequiredViewAsType(source, R.id.textViewUsername, "field 'textViewUsername'", TextView.class);
    target.textViewReviewDate = Utils.findRequiredViewAsType(source, R.id.textViewReviewDate, "field 'textViewReviewDate'", TextView.class);
    target.textViewReviewComment = Utils.findRequiredViewAsType(source, R.id.textViewReviewComment, "field 'textViewReviewComment'", TextView.class);
    target.textViewClassRating = Utils.findRequiredViewAsType(source, R.id.textViewClassRating, "field 'textViewClassRating'", TextView.class);
    target.recyclerViewGallery = Utils.findRequiredViewAsType(source, R.id.recyclerViewGallery, "field 'recyclerViewGallery'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RatingViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewUserProfile = null;
    target.textViewUsername = null;
    target.textViewReviewDate = null;
    target.textViewReviewComment = null;
    target.textViewClassRating = null;
    target.recyclerViewGallery = null;
  }
}
