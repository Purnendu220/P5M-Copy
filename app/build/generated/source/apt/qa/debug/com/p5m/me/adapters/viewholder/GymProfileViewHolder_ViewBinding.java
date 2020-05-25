// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.Button;
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

public class GymProfileViewHolder_ViewBinding implements Unbinder {
  private GymProfileViewHolder target;

  @UiThread
  public GymProfileViewHolder_ViewBinding(GymProfileViewHolder target, View source) {
    this.target = target;

    target.imageViewProfile = Utils.findRequiredViewAsType(source, R.id.imageViewProfile, "field 'imageViewProfile'", ImageView.class);
    target.imageViewCover = Utils.findRequiredViewAsType(source, R.id.imageViewCover, "field 'imageViewCover'", ImageView.class);
    target.imageViewMap = Utils.findRequiredViewAsType(source, R.id.imageViewMap, "field 'imageViewMap'", ImageView.class);
    target.button = Utils.findRequiredViewAsType(source, R.id.button, "field 'button'", Button.class);
    target.textViewName = Utils.findRequiredViewAsType(source, R.id.textViewName, "field 'textViewName'", TextView.class);
    target.textViewClassCategory = Utils.findRequiredViewAsType(source, R.id.textViewClassCategory, "field 'textViewClassCategory'", TextView.class);
    target.textViewTrainers = Utils.findRequiredViewAsType(source, R.id.textViewTrainers, "field 'textViewTrainers'", TextView.class);
    target.textViewGallery = Utils.findRequiredViewAsType(source, R.id.textViewGallery, "field 'textViewGallery'", TextView.class);
    target.textViewLocation = Utils.findRequiredViewAsType(source, R.id.textViewLocation, "field 'textViewLocation'", TextView.class);
    target.textViewLocationSub = Utils.findRequiredViewAsType(source, R.id.textViewLocationSub, "field 'textViewLocationSub'", TextView.class);
    target.textViewLocationPhone = Utils.findRequiredViewAsType(source, R.id.textViewLocationPhone, "field 'textViewLocationPhone'", TextView.class);
    target.textViewMore = Utils.findRequiredViewAsType(source, R.id.textViewMore, "field 'textViewMore'", TextView.class);
    target.textViewInfo = Utils.findRequiredViewAsType(source, R.id.textViewInfo, "field 'textViewInfo'", TextView.class);
    target.layoutDesc = Utils.findRequiredView(source, R.id.layoutDesc, "field 'layoutDesc'");
    target.layoutGallery = Utils.findRequiredView(source, R.id.layoutGallery, "field 'layoutGallery'");
    target.layoutLocationPhone = Utils.findRequiredView(source, R.id.layoutLocationPhone, "field 'layoutLocationPhone'");
    target.layoutMap = Utils.findRequiredView(source, R.id.layoutMap, "field 'layoutMap'");
    target.recyclerViewGallery = Utils.findRequiredViewAsType(source, R.id.recyclerViewGallery, "field 'recyclerViewGallery'", RecyclerView.class);
    target.linearLayoutTotalLocations = Utils.findRequiredViewAsType(source, R.id.linearLayoutTotalLocations, "field 'linearLayoutTotalLocations'", LinearLayout.class);
    target.textViewTotalLocations = Utils.findRequiredViewAsType(source, R.id.textViewTotalLocations, "field 'textViewTotalLocations'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GymProfileViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewProfile = null;
    target.imageViewCover = null;
    target.imageViewMap = null;
    target.button = null;
    target.textViewName = null;
    target.textViewClassCategory = null;
    target.textViewTrainers = null;
    target.textViewGallery = null;
    target.textViewLocation = null;
    target.textViewLocationSub = null;
    target.textViewLocationPhone = null;
    target.textViewMore = null;
    target.textViewInfo = null;
    target.layoutDesc = null;
    target.layoutGallery = null;
    target.layoutLocationPhone = null;
    target.layoutMap = null;
    target.recyclerViewGallery = null;
    target.linearLayoutTotalLocations = null;
    target.textViewTotalLocations = null;
  }
}
