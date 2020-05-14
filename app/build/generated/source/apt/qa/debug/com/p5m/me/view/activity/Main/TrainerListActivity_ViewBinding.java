// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TrainerListActivity_ViewBinding implements Unbinder {
  private TrainerListActivity target;

  private View view7f0a017f;

  @UiThread
  public TrainerListActivity_ViewBinding(TrainerListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TrainerListActivity_ViewBinding(final TrainerListActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.imageViewBack, "method 'imageViewBack'");
    view7f0a017f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imageViewBack(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0a017f.setOnClickListener(null);
    view7f0a017f = null;
  }
}
