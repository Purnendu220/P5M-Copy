// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.youtube.player.YouTubePlayerView;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VideoPlayerActivity_ViewBinding implements Unbinder {
  private VideoPlayerActivity target;

  @UiThread
  public VideoPlayerActivity_ViewBinding(VideoPlayerActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VideoPlayerActivity_ViewBinding(VideoPlayerActivity target, View source) {
    this.target = target;

    target.youTubePlayerView = Utils.findRequiredViewAsType(source, R.id.youtubePlayer, "field 'youTubePlayerView'", YouTubePlayerView.class);
    target.videoView = Utils.findRequiredViewAsType(source, R.id.video_view, "field 'videoView'", VideoView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    VideoPlayerActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.youTubePlayerView = null;
    target.videoView = null;
  }
}
