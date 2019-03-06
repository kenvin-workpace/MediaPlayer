package com.whz.mediaplayer.video;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.whz.mediaplayer.R;

/**
 * Created by kevin on 2018/4/30
 */
public class Video2Activity extends AppCompatActivity {

    private VideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video2);

        initView();
        initSetting();
    }

    private void initSetting() {
        //设置播放的来源
        Uri uri = Uri.parse("http://192.168.1.104:8080/video/00.mp4");
        mVideoView.setVideoURI(uri);
        //实例化媒体控制器
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);
        mVideoView.start();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mVideoView = findViewById(R.id.video_view);
    }

}
