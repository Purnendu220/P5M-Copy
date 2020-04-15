package com.p5m.me.agorartc.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.p5m.me.R;
import com.p5m.me.agorartc.adapter.SmallVideoViewAdapter;
import com.p5m.me.agorartc.listeners.OnDoubleTapListener;
import com.p5m.me.agorartc.listeners.VideoViewEventListener;
import com.p5m.me.agorartc.stats.LocalStatsData;
import com.p5m.me.agorartc.stats.RemoteStatsData;
import com.p5m.me.agorartc.stats.StatsData;
import com.p5m.me.agorartc.stats.VideoStatusData;
import com.p5m.me.agorartc.ui.VideoGridContainer;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;

import java.util.HashMap;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class LiveActivity extends RtcBaseActivity {
    private static final String TAG = LiveActivity.class.getSimpleName();

    private VideoGridContainer mVideoGridContainer;
    private ImageView mMuteAudioBtn;
    private ImageView mMuteVideoBtn;
    private ImageView mSpeakerPhoneButton;

    private VideoEncoderConfiguration.VideoDimensions mVideoDimension;
    private ClassModel classModel;

    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid

    private SmallVideoViewAdapter mSmallVideoViewAdapter;
    private RecyclerView recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.turnScreenOnThroughKeyguard(this);
        setContentView(R.layout.activity_live_room);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initUI();
        initData();
    }

    private void initUI() {
        TextView roomName = findViewById(R.id.live_room_name);
        TextView live_room_broadcaster_uid = findViewById(R.id.live_room_broadcaster_uid);

        roomName.setText(channelName);
        roomName.setSelected(true);
        
        initUserIcon();

        int role = getIntent().getIntExtra(
                AppConstants.KEY_CLIENT_ROLE,
                Constants.CLIENT_ROLE_AUDIENCE);
        classModel = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_MODEL);
        if(classModel!=null){
            live_room_broadcaster_uid.setText(classModel.getTitle());
        }else{
            live_room_broadcaster_uid.setText("");

        }
        boolean isBroadcaster =  (role == Constants.CLIENT_ROLE_BROADCASTER);

        mMuteVideoBtn = findViewById(R.id.live_btn_mute_video);
        mMuteVideoBtn.setActivated(isBroadcaster);

        mMuteAudioBtn = findViewById(R.id.live_btn_mute_audio);
        mMuteAudioBtn.setActivated(isBroadcaster);

        mSpeakerPhoneButton = findViewById(R.id.live_btn_push_stream);
        mSpeakerPhoneButton.setActivated(true);

        ImageView beautyBtn = findViewById(R.id.live_btn_beautification);
        beautyBtn.setActivated(true);
        rtcEngine().setBeautyEffectOptions(beautyBtn.isActivated(),
                AppConstants.DEFAULT_BEAUTY_OPTIONS);

        mVideoGridContainer = findViewById(R.id.live_video_grid_layout);
        mVideoGridContainer.setStatsManager(statsManager());
        mVideoGridContainer.setOnTouchListener(new OnDoubleTapListener(context) {
            @Override
            public void onDoubleTap(View view, MotionEvent e) {

                switchToSmallView();
            }

            @Override
            public void onSingleTapUp() {
            }
        });

        rtcEngine().setClientRole(role);
        rtcEngine().setEnableSpeakerphone(true);
    if (isBroadcaster) startBroadcast();
    }

    private void initUserIcon() {
        Bitmap origin = BitmapFactory.decodeResource(getResources(), R.drawable.fake_user_icon);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), origin);
        drawable.setCircular(true);
        ImageView iconView = findViewById(R.id.live_name_board_icon);
        iconView.setImageDrawable(drawable);
    }

    private void initData() {
        mVideoDimension = AppConstants.VIDEO_DIMENSIONS[
                config().getVideoDimenIndex()];
    }

    @Override
    protected void onGlobalLayoutCompleted() {
        RelativeLayout topLayout = findViewById(R.id.live_room_top_layout);
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) topLayout.getLayoutParams();
        params.height = mStatusBarHeight + topLayout.getMeasuredHeight();
        topLayout.setLayoutParams(params);
        topLayout.setPadding(0, mStatusBarHeight, 0, 0);
    }

    private void startBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        SurfaceView surface = prepareRtcVideo(0, true,VideoCanvas.RENDER_MODE_HIDDEN);
        mUidsList.put(0,surface);
        bindToSmallVideoView(0);
        mMuteAudioBtn.setActivated(true);

    }

    private void stopBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
        removeRtcVideo(0, true);
        mVideoGridContainer.removeUserVideoLayout(0, true);
        mUidsList.remove(0);
        mMuteAudioBtn.setActivated(false);
        bindToSmallVideoView(0);

    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        // Do nothing at the moment
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        // Do nothing at the moment
    }

    @Override
    public void onUserOffline(final int uid, int reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeRemoteUser(uid);
            }
        });
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                renderRemoteUser(uid);
            }
        });
    }

    private void renderRemoteUser(int uid) {
        if(uid == classModel.getGymBranchDetail().getGymId()||(classModel.getTrainerDetail()!=null&&uid == classModel.getTrainerDetail().getId())){
            LogUtils.debug("Video BROADCAST UID "+uid);
            SurfaceView surface = prepareRtcVideo(uid, false);
            switchToLargeView(new VideoStatusData(uid,surface, VideoStatusData.DEFAULT_STATUS, VideoStatusData.DEFAULT_VOLUME));
        }else{
            SurfaceView surface = prepareRtcVideo(uid, false,VideoCanvas.RENDER_MODE_HIDDEN);
            mUidsList.put(uid,surface);
            bindToSmallVideoView(uid);
        }

    }

    private void removeRemoteUser(int uid) {
        removeRtcVideo(uid, false);
        mVideoGridContainer.removeUserVideoLayout(uid, false);
        mUidsList.remove(uid);
        bindToSmallVideoView(uid);
    }

    @Override
    public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setWidth(mVideoDimension.width);
        data.setHeight(mVideoDimension.height);
        data.setFramerate(stats.sentFrameRate);
    }

    @Override
    public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {
        if (!statsManager().isEnabled()) return;

        LocalStatsData data = (LocalStatsData) statsManager().getStatsData(0);
        if (data == null) return;

        data.setLastMileDelay(stats.lastmileDelay);
        data.setVideoSendBitrate(stats.txVideoKBitRate);
        data.setVideoRecvBitrate(stats.rxVideoKBitRate);
        data.setAudioSendBitrate(stats.txAudioKBitRate);
        data.setAudioRecvBitrate(stats.rxAudioKBitRate);
        data.setCpuApp(stats.cpuAppUsage);
        data.setCpuTotal(stats.cpuAppUsage);
        data.setSendLoss(stats.txPacketLossRate);
        data.setRecvLoss(stats.rxPacketLossRate);
    }

    @Override
    public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
        if (!statsManager().isEnabled()) return;

        StatsData data = statsManager().getStatsData(uid);
        if (data == null) return;

        data.setSendQuality(statsManager().qualityToString(txQuality));
        data.setRecvQuality(statsManager().qualityToString(rxQuality));
    }

    @Override
    public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {
        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setWidth(stats.width);
        data.setHeight(stats.height);
        data.setFramerate(stats.rendererOutputFrameRate);
        data.setVideoDelay(stats.delay);
    }

    @Override
    public void onRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats stats) {
        if (!statsManager().isEnabled()) return;

        RemoteStatsData data = (RemoteStatsData) statsManager().getStatsData(stats.uid);
        if (data == null) return;

        data.setAudioNetDelay(stats.networkTransportDelay);
        data.setAudioNetJitter(stats.jitterBufferDelay);
        data.setAudioLoss(stats.audioLossRate);
        data.setAudioQuality(statsManager().qualityToString(stats.quality));
    }

    @Override
    public void finish() {
        super.finish();
        statsManager().clearAllData();
    }

    public void onLeaveClicked(View view) {
        finish();
    }

    public void onSwitchCameraClicked(View view) {
        rtcEngine().switchCamera();
    }

    public void onBeautyClicked(View view) {
        view.setActivated(!view.isActivated());
        rtcEngine().setBeautyEffectOptions(view.isActivated(),
                AppConstants.DEFAULT_BEAUTY_OPTIONS);
    }

    public void onMoreClicked(View view) {
        // Do nothing at the moment
    }

    public void onPushStreamClicked(View view) {
        view.setActivated(!view.isActivated());
        rtcEngine().setEnableSpeakerphone(view.isActivated());
    }

    public void onMuteAudioClicked(View view) {
        if (!mMuteVideoBtn.isActivated()) return;

        rtcEngine().muteLocalAudioStream(view.isActivated());
        view.setActivated(!view.isActivated());
    }

    public void onSpeakerPhoneEnabled(){

    }

    public void onMuteVideoClicked(View view) {
        if (view.isActivated()) {
            stopBroadcast();
        } else {
            startBroadcast();
        }
        view.setActivated(!view.isActivated());
    }

    private void bindToSmallVideoView(int exceptUid) {

        recycler = findViewById(R.id.small_video_view_container);
        if(mUidsList!=null&&mUidsList.size()>0){
            recycler.setVisibility(View.VISIBLE);

        }else{
            recycler.setVisibility(View.GONE);

        }
        boolean create = false;

        if (mSmallVideoViewAdapter == null) {
            create = true;
            mSmallVideoViewAdapter = new SmallVideoViewAdapter(this, exceptUid, mUidsList, new VideoViewEventListener() {
                @Override
                public void onItemDoubleClick(View v, Object item) {
                    switchToLargeView((VideoStatusData) item);
                }
            },classModel);
            mSmallVideoViewAdapter.setHasStableIds(true);
        }
        recycler.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(horizontalLayoutManagaer);
        //recycler.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        recycler.setAdapter(mSmallVideoViewAdapter);

        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        if (!create) {
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
        }

        //  mSmallVideoViewDock.setVisibility(View.VISIBLE);
    }

    private void switchToLargeView(VideoStatusData data){
        VideoStatusData largeViewdata = mVideoGridContainer.getSurface();
        if(largeViewdata!=null){
            mVideoGridContainer.removeUserVideo(largeViewdata.mUid,largeViewdata.mUid == 0?true:false);
            mUidsList.remove(data.mUid);
            SurfaceView surface = prepareRtcVideo(largeViewdata.mUid, largeViewdata.mUid == 0?true:false,VideoCanvas.RENDER_MODE_HIDDEN);
            mUidsList.put(largeViewdata.mUid,surface);
            bindToSmallVideoView(largeViewdata.mUid);
            mVideoGridContainer.addUserVideoSurface(data.mUid,prepareRtcVideo(data.mUid, data.mUid == 0?true:false),data.mUid == 0?true:false);
        }else{
            mUidsList.remove(data.mUid);
            bindToSmallVideoView(data.mUid);
            SurfaceView surface = prepareRtcVideo(data.mUid, data.mUid == 0?true:false,VideoCanvas.RENDER_MODE_FIT);

            mVideoGridContainer.addUserVideoSurface(data.mUid,surface,data.mUid == 0?true:false);
        }
    }
    private void switchToSmallView(){
        VideoStatusData largeViewdata = mVideoGridContainer.getSurface();
          if(largeViewdata!=null){
              boolean isLocal = largeViewdata.mUid == 0?true:false;
              mVideoGridContainer.removeUserVideoLayout(largeViewdata.mUid,isLocal);
              SurfaceView surface = prepareRtcVideo(largeViewdata.mUid, largeViewdata.mUid == 0?true:false,VideoCanvas.RENDER_MODE_HIDDEN);
              mUidsList.put(largeViewdata.mUid,surface);
              bindToSmallVideoView(largeViewdata.mUid);

          }

    }


}