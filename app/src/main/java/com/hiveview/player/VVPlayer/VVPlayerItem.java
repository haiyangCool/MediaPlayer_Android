package com.hiveview.player.VVPlayer;

public class VVPlayerItem {

    /** 已播放时间，在未开始播放前为0*/
    double playbackTime;

    /** 视频时长，未加载完成资源时，为0*/
    double duration;

    /** 缓存加载量*/
    double buffer;

    /** 视频地址*/
    String url;

    /** 视频静态资源*/
    VVPlayerAsset asset;

    public VVPlayerItem() {}

    public VVPlayerItem(String url) {
        this.url = url;
    }

    public VVPlayerItem(VVPlayerAsset asset) {
        this.asset = asset;
        this.url = this.asset.getUrl();
    }

    /** Set Note: you always do not run set Methods*/
    public void setPlaybackTime(double playbackTime) {
        this.playbackTime = playbackTime;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setBuffer(double buffer) {
        this.buffer = buffer;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAsset(VVPlayerAsset asset) {
        this.asset = asset;
    }

    /** Get
     * */
    public double getPlaybackTime() {
        return playbackTime;
    }

    public double getDuration() {
        return duration;
    }

    public double getBuffer() {
        return buffer;
    }

    public String getUrl() {
        return url;
    }

    public VVPlayerAsset getAsset() {
        return asset;
    }
}
