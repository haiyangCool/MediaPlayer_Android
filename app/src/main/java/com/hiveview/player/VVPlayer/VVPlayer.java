package com.hiveview.player.VVPlayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.hiveview.player.VVPlayer.VVPlayerInterface.Player;
import com.hiveview.player.VVPlayer.VVPlayerInterface.PlayerLoad;
import com.hiveview.player.VVPlayer.VVPlayerInterface.PlayerOperation;
import com.hiveview.player.VVPlayer.VVPlayerInterface.PlayerState;
import com.hiveview.player.VVPlayer.VVPlayerInterface.PlayerTimeLine;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class VVPlayer extends View implements Player,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnCompletionListener,
        SurfaceHolder.Callback{

    /** Context*/
    private Context context;
    /** 系统播放器*/
    private MediaPlayer mediaPlayer;

    /** SurfaceView*/
    private SurfaceView surfaceView;

    /** 资源Item*/
    private VVPlayerItem currentPlayerItem;

    /** 是否正在播放*/
    private boolean playing;

    /** 是否全屏*/
    private boolean fullScreen;

    /** 是否锁定屏幕（手机端锁定屏幕时，禁止所有操作）*/
    private boolean lockScreen;

    /** 是否自动播放默认 true*/
    private boolean autoPlay;

    /** 是否重复播放*/
    private boolean loop;

    /** MediaPlayer 没有提供实时播放进度的方法，在这里通过定时器获取（这种方式并不好）*/
    private Timer timer;

    private WeakReference<PlayerOperation> operation;
    private WeakReference<PlayerLoad> load;
    private WeakReference<PlayerTimeLine> timeline;
    private WeakReference<PlayerState> state;


    public VVPlayer(Context context) {
        super(context);
        this.context = context;
    }

    /** Public methods by defined Player Interface*/
    @Override
    public void init(Context context,VVPlayerItem playerItem) {
        this.context = context;
        load(playerItem);
    }

    @Override
    public void play() {

        if (operation != null) {
            operation.get().willPlay(this);
        }

        mediaPlayer.start();
        playing = true;

        if (operation != null) {
            operation.get().didPlay(this);
        }
    }

    @Override
    public void pause() {

        if (operation != null) {
            operation.get().willPause(this);
        }

        mediaPlayer.pause();
        playing = false;

        if (operation != null) {
            operation.get().didPause(this);
        }
    }

    @Override
    public void stop() {

        if (operation != null) {
            operation.get().willStopPlay(this);
        }

        mediaPlayer.stop();
        playing = false;

        if (operation != null) {
            operation.get().didStopPlay(this);
        }

    }

    @Override
    public void seekTo(double time) {

    }

    @Override
    public void replace(VVPlayerItem playerItem) {

        if (playerItem != null) { return; }
        if (playerItem.getUrl().equals(currentPlayerItem.getUrl())) {return;}
        load(playerItem);
    }

    /** MediaPlayer 监听器方法*********************************************/

    /** MediaPlayer OnPreparedListener 加载(异步)监听*/
    @Override
    public void onPrepared(MediaPlayer mp) {

        double duration = mediaPlayer.getDuration();
        currentPlayerItem.setDuration(duration);

        if (timeline != null) {

            timeline.get().playbackTime(this,0);
            timeline.get().duration(this,duration);
            timeline.get().bufferProgress(this,0);
            timeline.get().progress(this,0);
        }

        if (load != null) {
            load.get().readyToPlay(this);
        }
        if (autoPlay) {
            play();
        }
    }

    /** 缓存监听*/
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        if (timeline != null) {
            currentPlayerItem.setBuffer(percent);
            timeline.get().bufferProgress(this,percent);
        }
    }

    /** 播放进度（快进、快退）*/
    @Override
    public void onSeekComplete(MediaPlayer mp) {

        // 快进快退寻址完成后，如果寻址中途用户点击了暂停，则寻址完成后也暂停
        if (playing) {
            mp.start();
        }else {
            mp.pause();
        }

    }

    /** 播放完成监听*/
    @Override
    public void onCompletion(MediaPlayer mp) {

        // 时间线回归
        if (timeline != null) {
            timeline.get().playbackTime(this,0);
            timeline.get().duration(this,0);
            timeline.get().progress(this,0);
        }

        if (state != null) {
            state.get().playDidFinish(this);
        }

    }

    /** 播放出现错误*/
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        if (state != null) {
            state.get().playDidFaild(this);
        }
        return false;
    }

    /** SurfaceHolder CallBack*/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

/** Private methods about MediaPlayer*********************************/
    /** 加载播放资源*/
    private void load(VVPlayerItem playerItem) {
        currentPlayerItem = playerItem;
        String url = currentPlayerItem.getUrl();
        if (url == null) {
            if (operation != null) {
                if (load != null) {
                    load.get().loadFaild(this,"地址为空");
                }
            }
            return;
        }

        mediaPlayer = player();

        if (load != null) {
            load.get().loading(this);
        }

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnErrorListener(this);

            surfaceView = new SurfaceView(context);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);

        }catch (Exception e) {
            if (load != null) {
                load.get().loadFaild(this,e.getLocalizedMessage());
            }
        }

    }

    private MediaPlayer player() {
        if (mediaPlayer != null) {
            return mediaPlayer;
        }
        return new MediaPlayer();
    }

    /** Timer 定时任务(播放进度)******/

    private void startTimeTask() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new  Timer();
        timer.schedule(new TimelineTask(new WeakReference<VVPlayer>(this)), 0,1000);
    }

    private static class TimelineTask extends TimerTask {

        WeakReference<VVPlayer> player;

        public TimelineTask(WeakReference<VVPlayer> player) {
            this.player = player;
        }
        @Override
        public void run() {
            if (player != null && player.get().timeline != null) {

                int playbackTime = player.get().mediaPlayer.getCurrentPosition();
                int duration = player.get().mediaPlayer.getDuration();
                float progress = 0;
                if (duration != 0) {
                    progress = (float)playbackTime/(float) duration;
                }
                player.get().currentPlayerItem.setPlaybackTime(playbackTime);
                player.get().currentPlayerItem.setDuration(duration);
                player.get().timeline.get().progress(player.get(),progress);
                player.get().timeline.get().playbackTime(player.get(),playbackTime);
            }
        }
    }

    /** Set and Get so ugly*/
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void setOperation(WeakReference<PlayerOperation> operation) {
        this.operation = operation;
    }

    public void setLoad(WeakReference<PlayerLoad> load) {
        this.load = load;
    }

    public void setTimeline(WeakReference<PlayerTimeLine> timeline) {
        this.timeline = timeline;
    }

    public void setState(WeakReference<PlayerState> state) {
        this.state = state;
    }

    public VVPlayerItem getCurrentPlayerItem() {
        return currentPlayerItem;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public boolean isLockScreen() {
        return lockScreen;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public boolean isLoop() {
        return loop;
    }
}
