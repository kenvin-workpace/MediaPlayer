package com.whz.mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.whz.mediaplayer.image.ImageActivity;
import com.whz.mediaplayer.music.MusicActivity;
import com.whz.mediaplayer.video.Video1Activity;
import com.whz.mediaplayer.video.Video2Activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_music:
                enterActivity(MusicActivity.class);
                break;
            case R.id.btn_video1:
                enterActivity(Video1Activity.class);
                break;
            case R.id.btn_video2:
                enterActivity(Video2Activity.class);
                break;
            case R.id.btn_image:
                enterActivity(ImageActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 转入Activity
     */
    private void enterActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }


    /**
     * 初始化View
     */
    private void initView() {
        findById(R.id.btn_music);
        findById(R.id.btn_video1);
        findById(R.id.btn_video2);
        findById(R.id.btn_image);
    }

    /**
     * 查找View id
     */
    private void findById(int id) {
        findViewById(id).setOnClickListener(this);
    }

}
