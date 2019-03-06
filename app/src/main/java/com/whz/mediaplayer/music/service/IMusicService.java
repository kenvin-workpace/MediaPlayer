package com.whz.mediaplayer.music.service;

/**
 * Created by kevin on 2018/4/30
 */
public interface IMusicService {
    void playMusicApi();

    void pauseMusicApi();

    void continueMusicApi();

    void stopMusicApi();

    void seekToPositionApi(int position);
}
