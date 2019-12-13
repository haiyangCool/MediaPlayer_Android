package com.hiveview.player.VVPlayer.VVPlayerInterface;

import com.hiveview.player.VVPlayer.VVPlayer;

/** 播放器播放操作*/
public interface PlayerOperation {

    /** 即将播放*/
    public void willPlay(VVPlayer player);

    /** 已播放*/
    public void didPlay(VVPlayer player);

    /** 即将暂停*/
    public void willPause(VVPlayer player);

    /** 已暂停*/
    public void didPause(VVPlayer player);

    /** 即将停止播放*/
    public void willStopPlay(VVPlayer player);

    /** 停止播放（播放器销毁）*/
    public void didStopPlay(VVPlayer player);
}
