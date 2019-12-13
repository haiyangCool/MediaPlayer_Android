package com.hiveview.player.VVPlayer;

import android.content.Context;
import android.view.View;

/** 播放器 操作浮层
 * 在该层仅定义
 * 顶部操作横幅和底部操作横幅（仅为空白横幅，方便统一管理其他操作按钮，-播放|暂停，时间进度条等）
 * 手势 （单击、双击）
 * 轻扫手势 （比如手机，左半边屏幕调节屏幕亮度，右半边调节播放音量）
 * */
public class VVCoverView extends View {
    public VVCoverView(Context context) {
        super(context);
    }
}
