// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.custom;

import android.view.View;
import android.widget.ProgressBar;
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

public class ShowSchedulesBootomDialogFragment_ViewBinding implements Unbinder {
  private ShowSchedulesBootomDialogFragment target;

  @UiThread
  public ShowSchedulesBootomDialogFragment_ViewBinding(ShowSchedulesBootomDialogFragment target,
      View source) {
    this.target = target;

    target.recycleViewShowSchedule = Utils.findRequiredViewAsType(source, R.id.recycleViewShowSchedule, "field 'recycleViewShowSchedule'", RecyclerView.class);
    target.layoutBottomSheet = Utils.findRequiredViewAsType(source, R.id.layoutBottomSheet, "field 'layoutBottomSheet'", ConstraintLayout.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
    target.textViewGymBranch = Utils.findRequiredViewAsType(source, R.id.textViewGymBranch, "field 'textViewGymBranch'", TextView.class);
    target.textViewNoClass = Utils.findRequiredViewAsType(source, R.id.textViewNoClass, "field 'textViewNoClass'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShowSchedulesBootomDialogFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recycleViewShowSchedule = null;
    target.layoutBottomSheet = null;
    target.progressBar = null;
    target.textViewGymBranch = null;
    target.textViewNoClass = null;
  }
}
