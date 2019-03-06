package com.whz.mediaplayer.video;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.whz.mediaplayer.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.whz.mediaplayer.util.Util.formatTime;

/**
 * Created by kevin on 2018/4/30
 */
public class Video1Activity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

    private static TextView mDuration, mCurrent;
    private static SurfaceView mSurfaceView;
    private MediaPlayer mPlayer;
    private SurfaceHolder mSurfaceHolder;
    private SeekBar mSeekBar;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
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
    private TimerTask mTimerTask;
    private Timer mTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video1);

        initView();
        initSetting();
        initListener();
    }

    private void initListener() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPlayer.seekTo(seekBar.getProgress());
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

    private void initSetting() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startVideo();
                break;
            case R.id.btn_stop:
                stopVieo();
                break;
            case R.id.btn_pause:
                pauseVideo();
                break;
            case R.id.btn_continue:
                continueVideo();
                break;
            default:
                break;
        }
    }

    /**
     * 播放视频
     */
    private void startVideo() {
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource("http://192.168.1.104:8080/video/00.mp4");
            mPlayer.setDisplay(mSurfaceHolder);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 继续播放
     */
    private void continueVideo() {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    /**
     * 暂停播放
     */
    private void pauseVideo() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    /**
     * 停止播放
     */
    private void stopVieo() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;

            mTimer.cancel();
            mTimerTask.cancel();
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayer.start();
        updateProcess();
    }

    private void updateProcess() {
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
                handler.sendMessage(message);
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
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopVieo();
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
        mSurfaceView = findViewById(R.id.surface_view);

        mCurrent = findViewById(R.id.music_current);
        mDuration = findViewById(R.id.music_duration);
    }

    /**
     * 查找View id
     */
    private void findById(int id) {
        findViewById(id).setOnClickListener(this);
    }
}
