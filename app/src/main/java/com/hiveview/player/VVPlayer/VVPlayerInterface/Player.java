package com.hiveview.player.VVPlayer.VVPlayerInterface;

import android.content.Context;

import com.hiveview.player.VVPlayer.VVPlayerItem;

/** 播放器必须要实现的接口协议*/
public interface Player {

    /** 初始化播放资源*/
    public void init(Context context, VVPlayerItem playerItem);

    /** 播放*/
    public void play();

    /** 暂停*/
    public void pause();

    /** 停止播放*/
    public void stop();

    /** seek to time*/
    public void seekTo(double time);

    /** 切换播放资源*/
    public void replace(VVPlayerItem playerItem);
}
