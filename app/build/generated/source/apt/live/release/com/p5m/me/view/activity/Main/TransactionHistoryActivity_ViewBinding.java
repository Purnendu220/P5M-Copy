// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TransactionHistoryActivity_ViewBinding implements Unbinder {
  private TransactionHistoryActivity target;

  private View view7f0a016a;

  @UiThread
  public TransactionHistoryActivity_ViewBinding(TransactionHistoryActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TransactionHistoryActivity_ViewBinding(final TransactionHistoryActivity target,
      View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipeRefreshLayout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.textViewEmptyLayoutText = Utils.findRequiredView(source, R.id.textViewEmptyLayoutText, "field 'textViewEmptyLayoutText'");
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
    TransactionHistoryActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.swipeRefreshLayout = null;
    target.textViewEmptyLayoutText = null;

    view7f0a016a.setOnClickListener(null);
    view7f0a016a = null;
  }
}
