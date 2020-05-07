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

public class MapGymViewHolder_ViewBinding implements Unbinder {
  private MapGymViewHolder target;

  @UiThread
  public MapGymViewHolder_ViewBinding(MapGymViewHolder target, View source) {
    this.target = target;

    target.textViewShowSchedule = Utils.findRequiredViewAsType(source, R.id.textViewShowSchedule, "field 'textViewShowSchedule'", TextView.class);
    target.imageViewOfGym = Utils.findRequiredViewAsType(source, R.id.imageViewOfGym, "field 'imageViewOfGym'", ImageView.class);
    target.textViewBranchName = Utils.findRequiredViewAsType(source, R.id.textViewBranchName, "field 'textViewBranchName'", TextView.class);
    target.textViewGymName = Utils.findRequiredViewAsType(source, R.id.textViewGymName, "field 'textViewGymName'", TextView.class);
    target.textViewCategoryRating = Utils.findRequiredViewAsType(source, R.id.textViewCategoryRating, "field 'textViewCategoryRating'", TextView.class);
    target.textViewRatingByUsers = Utils.findRequiredViewAsType(source, R.id.textViewRatingByUsers, "field 'textViewRatingByUsers'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MapGymViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewShowSchedule = null;
    target.imageViewOfGym = null;
    target.textViewBranchName = null;
    target.textViewGymName = null;
    target.textViewCategoryRating = null;
    target.textViewRatingByUsers = null;
  }
}
