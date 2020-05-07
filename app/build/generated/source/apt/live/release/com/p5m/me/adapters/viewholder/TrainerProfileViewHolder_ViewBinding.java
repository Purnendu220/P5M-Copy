// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TrainerProfileViewHolder_ViewBinding implements Unbinder {
  private TrainerProfileViewHolder target;

  @UiThread
  public TrainerProfileViewHolder_ViewBinding(TrainerProfileViewHolder target, View source) {
    this.target = target;

    target.imageViewProfile = Utils.findRequiredViewAsType(source, R.id.imageViewProfile, "field 'imageViewProfile'", ImageView.class);
    target.imageViewCover = Utils.findRequiredViewAsType(source, R.id.imageViewCover, "field 'imageViewCover'", ImageView.class);
    target.button = Utils.findRequiredViewAsType(source, R.id.button, "field 'button'", ImageButton.class);
    target.textViewName = Utils.findRequiredViewAsType(source, R.id.textViewName, "field 'textViewName'", TextView.class);
    target.textViewClassCategory = Utils.findRequiredViewAsType(source, R.id.textViewClassCategory, "field 'textViewClassCategory'", TextView.class);
    target.textViewFollowers = Utils.findRequiredViewAsType(source, R.id.textViewFollowers, "field 'textViewFollowers'", TextView.class);
    target.textViewGallery = Utils.findRequiredViewAsType(source, R.id.textViewGallery, "field 'textViewGallery'", TextView.class);
    target.textViewInfo = Utils.findRequiredViewAsType(source, R.id.textViewInfo, "field 'textViewInfo'", TextView.class);
    target.layoutDesc = Utils.findRequiredView(source, R.id.layoutDesc, "field 'layoutDesc'");
    target.layoutGallery = Utils.findRequiredView(source, R.id.layoutGallery, "field 'layoutGallery'");
    target.layoutLocation = Utils.findRequiredViewAsType(source, R.id.layoutLocation, "field 'layoutLocation'", LinearLayout.class);
    target.recyclerViewGallery = Utils.findRequiredViewAsType(source, R.id.recyclerViewGallery, "field 'recyclerViewGallery'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TrainerProfileViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewProfile = null;
    target.imageViewCover = null;
    target.button = null;
    target.textViewName = null;
    target.textViewClassCategory = null;
    target.textViewFollowers = null;
    target.textViewGallery = null;
    target.textViewInfo = null;
    target.layoutDesc = null;
    target.layoutGallery = null;
    target.layoutLocation = null;
    target.recyclerViewGallery = null;
  }
}
