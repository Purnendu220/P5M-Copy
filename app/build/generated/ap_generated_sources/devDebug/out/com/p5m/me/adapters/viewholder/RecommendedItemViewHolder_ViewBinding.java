// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RecommendedItemViewHolder_ViewBinding implements Unbinder {
  private RecommendedItemViewHolder target;

  @UiThread
  public RecommendedItemViewHolder_ViewBinding(RecommendedItemViewHolder target, View source) {
    this.target = target;

    target.recyclerViewRecommendedClass = Utils.findRequiredViewAsType(source, R.id.recyclerViewRecommendedClass, "field 'recyclerViewRecommendedClass'", RecyclerView.class);
    target.textViewRecomended = Utils.findRequiredViewAsType(source, R.id.textViewRecomended, "field 'textViewRecomended'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RecommendedItemViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerViewRecommendedClass = null;
    target.textViewRecomended = null;
  }
}
