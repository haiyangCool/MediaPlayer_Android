package com.hiveview.player.VVPlayer;

public class VVPlayerAsset {

    /** 视频地址*/
    String url;

    /** 视频静态封面*/
    String imageAddress;

    /** Asset 播放的速率（通常为1.0，但不一定）*/
    float perferedRata;

    /** Asset 播放的音量（通常为1.0，但不一定）*/
    float perferedVolumn;

    public VVPlayerAsset() {}

    public void init(String url) {
        this.url = url;
    }

    /** Set and Get*/
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }
}
