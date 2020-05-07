// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.custom;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MediaGalleryFragment_ViewBinding implements Unbinder {
  private MediaGalleryFragment target;

  @UiThread
  public MediaGalleryFragment_ViewBinding(MediaGalleryFragment target, View source) {
    this.target = target;

    target.imageViewImage = Utils.findRequiredViewAsType(source, R.id.imageViewImage, "field 'imageViewImage'", ImageView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MediaGalleryFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewImage = null;
    target.progressBar = null;
  }
}
