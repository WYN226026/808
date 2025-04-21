package tools;

import android.media.MediaPlayer;
import android.content.Context;

public class AudioPlayer {
    private MediaPlayer mediaPlayer;
    private Context context;

    public AudioPlayer(Context context) {
        this.context = context;
    }

    // 开始循环播放，如果已经播放则直接退出
    public void startLooping(int resId) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            // 已经在播放，直接退出
            return;
        }

        // 初始化 MediaPlayer
        mediaPlayer = MediaPlayer.create(context, resId);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true); // 设置循环播放
            mediaPlayer.start(); // 开始播放
        }
    }

    // 停止播放
    public void stopPlaying() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop(); // 停止播放
            }
            mediaPlayer.release(); // 释放资源
            mediaPlayer = null;
        }
    }
}
