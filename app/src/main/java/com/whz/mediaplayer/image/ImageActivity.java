package com.whz.mediaplayer.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.whz.mediaplayer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kevin on 2018/4/30
 */
public class ImageActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, Camera.PreviewCallback {

    private final String aTag = ImageActivity.class.getSimpleName();

    private ImageView mImageView;
    private File mPicPath;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    private String mPath = Environment.getExternalStorageDirectory() + File.separator + "images";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initView();
        initSetting();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_takepic1:
                btnTakepic1();
                break;
            case R.id.btn_takepic2:
                btnTakepic2();
                break;
            case R.id.btn_takepic3:
                btnTakepic3();
                break;
            case R.id.btn_picture_info:
                btnPictureInfo();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = Camera.open(getFrontCamera());

            if (mSurfaceHolder != null) {
                setPrewCamera(mCamera, mSurfaceHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setPrewCamera(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setPrewCamera(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        release();
    }


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        Camera.Size pictureSize = camera.getParameters().getPictureSize();
    }

    /**
     * 照相预览设置
     */
    private void setPrewCamera(Camera camera, SurfaceHolder surfaceHolder) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size previewSize = parameters.getSupportedPreviewSizes().get(0);
            Camera.Size pictureSize = parameters.getSupportedPictureSizes().get(0);

            parameters.setPreviewSize(previewSize.width, previewSize.height);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            camera.setParameters(parameters);

            camera.setPreviewDisplay(surfaceHolder);
            camera.setPreviewCallback(this);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放照相机资源
     */
    private void release() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 前置摄像头
     */
    private int getFrontCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int camerCounts = Camera.getNumberOfCameras();

        for (int i = 0; i < camerCounts; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 后置摄像头
     */
    private int getBackCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int camerCounts = Camera.getNumberOfCameras();

        for (int i = 0; i < camerCounts; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return i;
            }
        }
        return -1;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (data != null && data.getExtras() != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    mImageView.setImageBitmap(bitmap);
                }
                break;
            case 101:
                if (data != null) {
                    mImageView.setImageURI(Uri.fromFile(mPicPath));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取图片信息
     */
    private void btnPictureInfo() {
        try {
            File file = new File(mPath, "test.png");
            if (!file.exists()) {
                return;
            }

            ExifInterface exifInterface = new ExifInterface(mPath + "/test.png");
            String datetime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            String model = exifInterface.getAttribute(ExifInterface.TAG_MODEL);

            Toast.makeText(this, datetime + " , " + make + " , " + model, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 照相3
     */
    private void btnTakepic3() {
        mCamera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                //快门关闭时执行一些动作
            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //对图像的原始数据做一些处理
            }
        }, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    FileOutputStream fos = new FileOutputStream("sdcard/test.png");
                    fos.write(data);
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 照相2
     */
    private void btnTakepic2() {
        File file = new File(mPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        mPicPath = new File(file, "test.png");
        Uri uri = FileProvider.getUriForFile(this, "com.whz.image", mPicPath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, 101);
    }

    /**
     * 照相1
     */
    private void btnTakepic1() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }


    /**
     * 初始化照相机设置
     */
    private void initSetting() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    /**
     * 初始化View
     */
    private void initView() {
        findById(R.id.btn_takepic1);
        findById(R.id.btn_takepic2);
        findById(R.id.btn_takepic3);
        findById(R.id.btn_picture_info);

        mImageView = findViewById(R.id.iv_img);
        mSurfaceView = findViewById(R.id.surfaceview);
    }

    /**
     * 查找View id
     */
    private void findById(int id) {
        findViewById(id).setOnClickListener(this);
    }


}
