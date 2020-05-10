// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChooseFocusActivity_ViewBinding implements Unbinder {
  private ChooseFocusActivity target;

  private View view7f0a017d;

  private View view7f0a016a;

  @UiThread
  public ChooseFocusActivity_ViewBinding(ChooseFocusActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChooseFocusActivity_ViewBinding(final ChooseFocusActivity target, View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.progressBarDone = Utils.findRequiredViewAsType(source, R.id.progressBarDone, "field 'progressBarDone'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.imageViewDone, "field 'imageViewDone' and method 'imageViewDone'");
    target.imageViewDone = Utils.castView(view, R.id.imageViewDone, "field 'imageViewDone'", ImageView.class);
    view7f0a017d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imageViewDone(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.imageViewBack, "method 'imageViewBack'");
    view7f0a016a = view;
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
    ChooseFocusActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.progressBarDone = null;
    target.imageViewDone = null;

    view7f0a017d.setOnClickListener(null);
    view7f0a017d = null;
    view7f0a016a.setOnClickListener(null);
    view7f0a016a = null;
  }
}
