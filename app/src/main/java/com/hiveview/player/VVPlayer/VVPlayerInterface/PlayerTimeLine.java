package com.hiveview.player.VVPlayer.VVPlayerInterface;

import com.hiveview.player.VVPlayer.VVPlayer;

/** 播放器时间线*/
public interface PlayerTimeLine {

    /** 视频时长*/
    public void duration(VVPlayer player, double duration);

    /** 缓存*/
    public void buffer(VVPlayer player, double buffer);

    /** 已播放时间*/
    public void playbackTime(VVPlayer player, double playbackTime);

    /** 播放进度 progress
     * progress = (playbackTime)/duration*/
    public void progress(VVPlayer player, float progress);

    /** 缓存进度 bufferProgress
     * progress = buffer / duration*/
    public void bufferProgress(VVPlayer player, float progress);
}
