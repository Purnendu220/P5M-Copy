// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChooseNationalityActivity_ViewBinding implements Unbinder {
  private ChooseNationalityActivity target;

  private View view7f0a017f;

  @UiThread
  public ChooseNationalityActivity_ViewBinding(ChooseNationalityActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChooseNationalityActivity_ViewBinding(final ChooseNationalityActivity target,
      View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
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
    ChooseNationalityActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;

    view7f0a017f.setOnClickListener(null);
    view7f0a017f = null;
  }
}
