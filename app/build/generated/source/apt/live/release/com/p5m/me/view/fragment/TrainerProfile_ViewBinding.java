// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.fragment;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TrainerProfile_ViewBinding implements Unbinder {
  private TrainerProfile target;

  @UiThread
  public TrainerProfile_ViewBinding(TrainerProfile target, View source) {
    this.target = target;

    target.recyclerViewTrainerProfile = Utils.findRequiredViewAsType(source, R.id.recyclerViewProfile, "field 'recyclerViewTrainerProfile'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipeRefreshLayout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TrainerProfile target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerViewTrainerProfile = null;
    target.swipeRefreshLayout = null;
  }
}
