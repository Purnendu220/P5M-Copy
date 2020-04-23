package com.p5m.me.agorartc.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.util.ArrayUtils;
import com.p5m.me.BuildConfig;
import com.p5m.me.R;
import com.p5m.me.agorartc.adapter.SmallVideoViewAdapter;
import com.p5m.me.agorartc.listeners.OnDoubleTapListener;
import com.p5m.me.agorartc.listeners.VideoViewEventListener;
import com.p5m.me.agorartc.stats.LocalStatsData;
import com.p5m.me.agorartc.stats.RemoteStatsData;
import com.p5m.me.agorartc.stats.StatsData;
import com.p5m.me.agorartc.stats.VideoStatusData;
import com.p5m.me.agorartc.ui.VideoGridContainer;
import com.p5m.me.data.main.AgoraUserCount;
import com.p5m.me.data.main.AgoraUserStatus;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;

import java.util.HashMap;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

import static io.agora.rtc.Constants.REMOTE_VIDEO_STATE_DECODING;
import static io.agora.rtc.Constants.REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED;
import static io.agora.rtc.Constants.REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED;
import static io.agora.rtc.Constants.REMOTE_VIDEO_STATE_STOPPED;

public class LiveActivity extends RtcBaseActivity implements NetworkCommunicator.RequestListener {
    private static final String TAG = LiveActivity.class.getSimpleName();

    private VideoGridContainer mVideoGridContainer;
    private ImageView mMuteAudioBtn;
    private ImageView mMuteVideoBtn ,mMuteVideoBtnOnly;
    private ImageView mSpeakerPhoneButton;

    private VideoEncoderConfiguration.VideoDimensions mVideoDimension;
    private ClassModel classModel;

    private final HashMap<Integer, VideoStatusData> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid

    private SmallVideoViewAdapter mSmallVideoViewAdapter;
    private RecyclerView recycler;
    private Handler handler;
    private Runnable nextScreenRunnable;
    private long DELAY_NAVIGATION = 1000*60; // 1 sec
    TextView onLineUserCount;
    ImageView iconView;
    TextView roomName;
   // TextView textViewAlert;
    boolean isTrainerJoined;
    int trainerId;
    boolean isShowingAlert;
    boolean muteUnMuteActionPerformed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.turnScreenOnThroughKeyguard(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_live_room);
        handler = new Handler();

        initUI();
        initData();
    }

    private void initUI() {
        mMuteVideoBtnOnly = findViewById(R.id.live_btn_mute_video_only);
        roomName = findViewById(R.id.live_room_name);
        onLineUserCount = findViewById(R.id.live_room_broadcaster_uid);
        //roomName.setText(channelName);
        roomName.setSelected(true);
        
        initUserIcon();

        int role = getIntent().getIntExtra(
                AppConstants.KEY_CLIENT_ROLE,
                Constants.CLIENT_ROLE_AUDIENCE);
        classModel = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_MODEL);
        trainerId = classModel.getTrainerDetail()!=null&&classModel.getTrainerDetail().getId()>0?classModel.getTrainerDetail().getId():classModel.getGymBranchDetail().getBranchId();

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
        rtcEngine().setDefaultAudioRoutetoSpeakerphone(true);
        rtcEngine().enableDualStreamMode(true);
        rtcEngine().adjustPlaybackSignalVolume(200);


        rtcEngine().setLocalPublishFallbackOption(Constants.STREAM_FALLBACK_OPTION_VIDEO_STREAM_LOW);
        rtcEngine().setRemoteSubscribeFallbackOption(Constants.STREAM_FALLBACK_OPTION_VIDEO_STREAM_LOW);
    if (isBroadcaster) startBroadcast();
    }

    private void initUserIcon() {
        Bitmap origin = BitmapFactory.decodeResource(getResources(), R.drawable.fake_user_icon);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), origin);
        drawable.setCircular(true);
        ImageView iconView = findViewById(R.id.live_name_board_icon);
        iconView.setImageDrawable(drawable);
    }
    private void startSignaling() {
        nextScreenRunnable = () -> {
            networkCommunicator.getUserCountInChannel(BuildConfig.AGORA_APP_ID,channelName,true,this);
            startSignaling();
        };
        handler.postDelayed(nextScreenRunnable, DELAY_NAVIGATION);
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
        if(!mMuteAudioBtn.isActivated()){
            rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        }
        SurfaceView surface = prepareRtcVideo(0, true,VideoCanvas.RENDER_MODE_HIDDEN);
        mUidsList.put(0,new VideoStatusData(0, surface, VideoStatusData.DEFAULT_STATUS, VideoStatusData.DEFAULT_VOLUME));
        rtcEngine().muteLocalVideoStream(false);
        if(muteUnMuteActionPerformed){
           rtcEngine().muteLocalAudioStream(!mMuteAudioBtn.isActivated());
           mMuteAudioBtn.setActivated(mMuteAudioBtn.isActivated());
        }else{
            mMuteAudioBtn.setActivated(true);
        }
        bindToSmallVideoView(0);

    }

    private void stopBroadcast() {
        if(mMuteAudioBtn.isActivated()){
            rtcEngine().muteLocalVideoStream(true);
            removeRtcVideo(0, true);
            mVideoGridContainer.removeUserVideoLayout(0, true);
            mUidsList.remove(0);
            bindToSmallVideoView(0);
        }else{
            rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
            removeRtcVideo(0, true);
            mVideoGridContainer.removeUserVideoLayout(0, true);
            mUidsList.remove(0);
            mMuteAudioBtn.setActivated(false);
            bindToSmallVideoView(0);
        }

    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkCommunicator.getUserCountInChannel(BuildConfig.AGORA_APP_ID,channelName,true,LiveActivity.this);
                if(!isTrainerJoined){
                    int trainerId = classModel.getTrainerDetail()!=null&&classModel.getTrainerDetail().getId()>0?classModel.getTrainerDetail().getId():classModel.getGymBranchDetail().getBranchId();
                    networkCommunicator.getUserStatusInChannel(BuildConfig.AGORA_APP_ID,trainerId+"",channelName,true,LiveActivity.this);
                }

                startSignaling();

            }
        });

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
                isTrainerJoined = true;

        }else{
            SurfaceView surface = prepareRtcVideo(uid, false,VideoCanvas.RENDER_MODE_HIDDEN);
            mUidsList.put(uid,new VideoStatusData(uid, surface, VideoStatusData.DEFAULT_STATUS, VideoStatusData.DEFAULT_VOLUME));
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roomName.setText(Helper.getNetworkQualityTx(rxQuality));
            }
        });
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
    public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed) {
        LogUtils.networkError(state+" onRemoteVideoStateChanged");
      switch (reason){
          case REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED:
              runOnUiThread(() -> removeRemoteUser(uid));
              break;
          case REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED:
              runOnUiThread(() -> renderRemoteUser(uid));

              break;
      }
    }

    @Override
    public void onRemoteAudioStateChanged(int uid, int state, int reason, int elapsed) {

    }

    @Override
    public void onRemoteSubscribeFallbackToAudioOnly(int uid, boolean isFallbackOrRecover) {

    }

    @Override
    public void onUserMuteVideo(int uid, boolean muted) {

    }

    @Override
    public void onLocalVideoStateChanged(int uid, int error) {
        LogUtils.networkError(error+" onLocalVideoStateChanged");
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
        if(view.isActivated()){
            rtcEngine().adjustPlaybackSignalVolume(100);

        }else{
            rtcEngine().adjustPlaybackSignalVolume(200);

        }
        view.setActivated(!view.isActivated());
        rtcEngine().setEnableSpeakerphone(view.isActivated());

    }

    public void onMuteAudioClicked(View view) {
        muteUnMuteActionPerformed = true;
        if(!mMuteVideoBtn.isActivated()){
           if(view.isActivated()){
               rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
           }else{
               rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
               rtcEngine().muteLocalVideoStream(true);
           }
        }
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

    public void onMuteVideoOnlyClicked(View view){
        if(view.isActivated()){
            rtcEngine().muteLocalVideoStream(true);


        }else{
            rtcEngine().muteLocalVideoStream(false);



        }
        view.setActivated(!view.isActivated());

    }

    private void bindToSmallVideoView(int exceptUid) {
        LogUtils.networkError("fdgsfdfgfdgfgdfgdfdgfgdf");

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
            if(largeViewdata.mUid==0&&!mMuteVideoBtnOnly.isActivated()){
                mUidsList.put(largeViewdata.mUid,new VideoStatusData(largeViewdata.mUid, surface, VideoStatusData.VIDEO_MUTED, VideoStatusData.DEFAULT_VOLUME));
            }else{
                mUidsList.put(largeViewdata.mUid,new VideoStatusData(largeViewdata.mUid, surface, VideoStatusData.DEFAULT_STATUS, VideoStatusData.DEFAULT_VOLUME));

            }
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
              if(largeViewdata.mUid==0&&!mMuteVideoBtnOnly.isActivated()){
                  mUidsList.put(largeViewdata.mUid,new VideoStatusData(largeViewdata.mUid, surface, VideoStatusData.VIDEO_MUTED, VideoStatusData.DEFAULT_VOLUME));
              }else{
                  mUidsList.put(largeViewdata.mUid,new VideoStatusData(largeViewdata.mUid, surface, VideoStatusData.DEFAULT_STATUS, VideoStatusData.DEFAULT_VOLUME));

              }
              bindToSmallVideoView(largeViewdata.mUid);

          }

    }
    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode){
            case NetworkCommunicator.RequestCode.GET_USER_COUNT_IN_CHANNEL:
                try{
                    AgoraUserCount  agoraUserCount = ((ResponseModel<AgoraUserCount>) response).data;
                    if(agoraUserCount!=null){
                        int count = (agoraUserCount.getBroadcasters().length+agoraUserCount.getAudience_total())-1;
                        boolean isTrainerAvailable = ArrayUtils.contains(agoraUserCount.getBroadcasters(), trainerId);
//
                        String countText = String.format(context.getResources().getString(R.string.online_count),count);
                        onLineUserCount.setText(countText);
                    }else{
                        onLineUserCount.setText("");

                    }
                }catch (Exception  e){
                    e.printStackTrace();
                }

                break;
            case NetworkCommunicator.RequestCode.GET_USER_STATUS_IN_CHANNEL:
                try{
                    AgoraUserStatus userStatus = ((ResponseModel<AgoraUserStatus>) response).data;
                    if(userStatus.isIn_channel()){
                        isTrainerJoined = true;
                    }else{
                        showAlert();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode){
            case NetworkCommunicator.RequestCode.GET_USER_COUNT_IN_CHANNEL:

                break;
            case NetworkCommunicator.RequestCode.GET_USER_STATUS_IN_CHANNEL:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(nextScreenRunnable);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(nextScreenRunnable);

    }

    private void showAlert(){
        if(!isTrainerJoined&&!isShowingAlert){
             DialogUtils.showBasicMessage(context, context.getResources().getString(R.string.user_welcome), context.getResources().getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
                 @Override
                 public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                     dialog.dismiss();
                     isShowingAlert = false;

                 }
             });

            isShowingAlert = true;
        }


    }

}
