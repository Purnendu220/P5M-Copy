package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.OnVideoSizeChangedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.p5m.me.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerActivity extends YouTubeBaseActivity implements OnPreparedListener, YouTubePlayer.OnInitializedListener {

    private static String url;
    @BindView(R.id.youtubePlayer)
    YouTubePlayerView youTubePlayerView;
    @BindView(R.id.video_view)
    VideoView videoView;
    private YouTubePlayer youTubePlayer;
    private int currentTimeMillis;
    private long currentTimeVideoView;
    static final String CURRENT_PLAY_TIME = "current_play_time";
    private String VIDEO_ID;

    public static void openActivity(Context context, String url) {
        VideoPlayerActivity.url = url;
        context.startActivity(new Intent(context, VideoPlayerActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        playVideo();

    }

    private void playVideo() {
        if (url != null) {
            if (url.contains("youtu.be")) {
                setupYoutubePlayer();
                youTubePlayerView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
            } else {
                setupVideoView();
                youTubePlayerView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupYoutubePlayer() {
        youTubePlayerView.initialize(getString(R.string.google_android_map_api_key), this);

    }

    private void setupVideoView() {
        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setOnPreparedListener(this);
        videoView.setVideoURI(Uri.parse(url));
    }

    @Override
    public void onPrepared() {
        videoView.start();
        videoView.setOnVideoSizedChangedListener(new OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(int intrinsicWidth, int intrinsicHeight, float pixelWidthHeightRatio) {

            }
        });
        videoView.seekTo(currentTimeVideoView);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onPause() {
        if (youTubePlayer != null)
            currentTimeMillis = youTubePlayer.getCurrentTimeMillis();
        videoView.pause();
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (youTubePlayer != null) {
            youTubePlayer.release();
        } else if (videoView != null)
            videoView.release();
        super.onDestroy();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        this.youTubePlayer = youTubePlayer;
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        VIDEO_ID = url.split("https://youtu.be/")[1];
        if (!wasRestored)
            youTubePlayer.loadVideo(VIDEO_ID, currentTimeMillis);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (youTubePlayer != null)
            outState.putInt(CURRENT_PLAY_TIME, youTubePlayer.getCurrentTimeMillis());
        if (videoView != null)
            outState.putLong(CURRENT_PLAY_TIME, videoView.getCurrentPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (youTubePlayer != null)
            currentTimeMillis = savedInstanceState.getInt(CURRENT_PLAY_TIME);
        if (videoView != null)
            currentTimeVideoView = savedInstanceState.getLong(CURRENT_PLAY_TIME);


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
