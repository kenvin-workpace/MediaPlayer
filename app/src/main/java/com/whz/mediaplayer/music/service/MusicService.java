package com.whz.mediaplayer.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.whz.mediaplayer.music.MusicActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kevin on 2018/4/30
 */
public class MusicService extends Service implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private String aTag = MusicService.class.getSimpleName();

    private MediaPlayer mPlayer;
    private TimerTask mTimerTask;
    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 播放音乐
     */
    public void playMusic() {
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource("http://192.168.1.104:8080/mp3/qiansixi.mp3");
            mPlayer.setOnBufferingUpdateListener(this);
            mPlayer.setOnErrorListener(this);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停播放
     */
    public void pauseMusic() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    /**
     * 继续播放
     */
    public void continueMusic() {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    /**
     * 停止播放
     */
    public void stopMusic() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;

            mTimer.cancel();
            mTimerTask.cancel();
        }
    }

    /**
     * 跳到某位置
     */
    public void seekToPosition(int position) {
        if (mPlayer != null) {
            mPlayer.seekTo(position);
        }
    }

    /**
     * 更新播放进度条
     */
    public void updateSeekBar() {

        final int duration = mPlayer.getDuration();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                int currentPosition = mPlayer.getCurrentPosition();

                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("duration", duration);
                bundle.putInt("currentPosition", currentPosition);
                message.setData(bundle);
                MusicActivity.mHandler.sendMessage(message);
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 0, 1000);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mTimer.cancel();
                mTimerTask.cancel();
            }
        });
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.e(aTag, "percent:" + percent);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(aTag, "what:" + what + " extra:" + extra);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        updateSeekBar();
    }

    private class MusicBinder extends Binder implements IMusicService {

        @Override
        public void playMusicApi() {
            playMusic();
        }

        @Override
        public void pauseMusicApi() {
            pauseMusic();
        }

        @Override
        public void continueMusicApi() {
            continueMusic();
        }

        @Override
        public void stopMusicApi() {
            stopMusic();
        }

        @Override
        public void seekToPositionApi(int position) {
            seekToPosition(position);
        }
    }
}
