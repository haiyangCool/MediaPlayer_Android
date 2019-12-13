package com.hiveview.player.VVPlayer.VVPlayerInterface;

import com.hiveview.player.VVPlayer.VVPlayer;

/** 播放器状态*/
public interface PlayerState {

    /** 播放卡顿，造成卡顿的原因可能会有网络延迟，或加载引擎等问题*/
    public void playStalled(VVPlayer player);

    /** 播放完成*/
    public void playDidFinish(VVPlayer player);

    /** 播放过程中出现错误导致失败*/
    public void playDidFaild(VVPlayer player);

    /** 播放器处于播放状态
     * 该状态的判定条件是
     * 1、播放器当前所处的状态为播放状态
     * 2、有可以继续播放的缓存资源（直播无法加载缓存时通过观察加载进度是否更新）
     * 该方法会在播放器播放中会一直回调，不要做耗时操作*/
    public void playing(VVPlayer player);

}
