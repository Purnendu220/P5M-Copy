// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClassMiniViewList_ViewBinding implements Unbinder {
  private ClassMiniViewList target;

  @UiThread
  public ClassMiniViewList_ViewBinding(ClassMiniViewList target, View source) {
    this.target = target;

    target.recyclerViewClass = Utils.findRequiredViewAsType(source, R.id.recyclerViewClass, "field 'recyclerViewClass'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipeRefreshLayout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.layoutNoData = Utils.findRequiredView(source, R.id.layoutNoData, "field 'layoutNoData'");
    target.imageViewEmptyLayoutImage = Utils.findRequiredViewAsType(source, R.id.imageViewEmptyLayoutImage, "field 'imageViewEmptyLayoutImage'", ImageView.class);
    target.buttonBook = Utils.findRequiredViewAsType(source, R.id.buttonBook, "field 'buttonBook'", Button.class);
    target.textViewEmptyLayoutText = Utils.findRequiredViewAsType(source, R.id.textViewEmptyLayoutText, "field 'textViewEmptyLayoutText'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ClassMiniViewList target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerViewClass = null;
    target.swipeRefreshLayout = null;
    target.layoutNoData = null;
    target.imageViewEmptyLayoutImage = null;
    target.buttonBook = null;
    target.textViewEmptyLayoutText = null;
  }
}
