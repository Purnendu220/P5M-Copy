// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class YouTubeViewHolder_ViewBinding implements Unbinder {
  private YouTubeViewHolder target;

  @UiThread
  public YouTubeViewHolder_ViewBinding(YouTubeViewHolder target, View source) {
    this.target = target;

    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", ConstraintLayout.class);
    target.textViewHeader = Utils.findRequiredViewAsType(source, R.id.textViewHeader, "field 'textViewHeader'", TextView.class);
    target.textViewSubHeader = Utils.findRequiredViewAsType(source, R.id.textViewSubHeader, "field 'textViewSubHeader'", TextView.class);
    target.textViewMore = Utils.findRequiredViewAsType(source, R.id.textViewMore, "field 'textViewMore'", TextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    YouTubeViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layout = null;
    target.textViewHeader = null;
    target.textViewSubHeader = null;
    target.textViewMore = null;
    target.recyclerView = null;
  }
}
