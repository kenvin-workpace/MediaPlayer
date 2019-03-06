package com.whz.mediaplayer.music;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.whz.mediaplayer.R;
import com.whz.mediaplayer.music.service.IMusicService;
import com.whz.mediaplayer.music.service.MusicService;

import static com.whz.mediaplayer.util.Util.formatTime;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    private static TextView mDuration, mCurrent;
    private static SeekBar mSeekBar;
    private IMusicService mService;

    @SuppressLint("HandlerLeak")
    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            int duration = data.getInt("duration");
            int currentPosition = data.getInt("currentPosition");

            mSeekBar.setMax(duration);
            mSeekBar.setProgress(currentPosition);

            mDuration.setText(formatTime(duration));
            mCurrent.setText(formatTime(currentPosition));
        }
    };
    private MusicConn mConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        initView();
        initSetting();
        initListener();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mService.playMusicApi();
                break;
            case R.id.btn_stop:
                mService.stopMusicApi();
                break;
            case R.id.btn_pause:
                mService.pauseMusicApi();
                break;
            case R.id.btn_continue:
                mService.continueMusicApi();
                break;
            default:
                break;
        }
    }

    /**
     * 监听SeekBar事件
     */
    private void initListener() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mService.seekToPositionApi(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * 初始化MediaPlayer
     */
    private void initSetting() {
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);

        mConn = new MusicConn();
        bindService(intent, mConn, BIND_AUTO_CREATE);
    }

    /**
     * 初始化View
     */
    private void initView() {
        findById(R.id.btn_start);
        findById(R.id.btn_stop);
        findById(R.id.btn_pause);
        findById(R.id.btn_continue);

        mSeekBar = findViewById(R.id.seek_bar);

        mCurrent = findViewById(R.id.music_current);
        mDuration = findViewById(R.id.music_duration);
    }

    /**
     * 查找View id
     */
    private void findById(int id) {
        findViewById(id).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConn);
    }

    private class MusicConn implements ServiceConnection {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = (IMusicService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    }
}
