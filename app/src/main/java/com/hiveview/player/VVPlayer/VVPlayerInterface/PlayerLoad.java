package com.hiveview.player.VVPlayer.VVPlayerInterface;

import com.hiveview.player.VVPlayer.VVPlayer;

/** 播放器加载状态*/
public interface PlayerLoad {

    /** 加载中*/
    public void loading(VVPlayer player);

    /** 加载成功准备播放*/
    public void readyToPlay(VVPlayer player);

    /** 加载失败*/
    public void loadFaild(VVPlayer player, String message);
}
