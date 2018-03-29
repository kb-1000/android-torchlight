package com.fake.android.torchlight.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.widget.Toast;

@SuppressWarnings("deprecation")
@SuppressLint("deprecation")
class CameraNormal extends com.fake.android.torchlight.camera.Camera {
    private Camera mCamera;
    private Camera.Parameters mCameraParams;
    private Context mContext;

    @Override
    public void init(Context context) {
        mContext = context;
        try {
            mCamera = Camera.open();
            mCameraParams = mCamera.getParameters();
        } catch (RuntimeException e) {
            CameraControl.noFlash();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void release() {
        try {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
                mCameraParams = null;
            }
        } catch (RuntimeException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void toggle(boolean enable) {
        if (mCamera == null) {
            return;
        }
        if (enable) {
            mCameraParams.setFlashMode("torch");
        } else {
            mCameraParams.setFlashMode("off");
        }
        try {
            mCamera.setParameters(mCameraParams);
        } catch (RuntimeException e) {
            if (e.getMessage().startsWith("set")) {
                CameraControl.noFlash();
            }
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            mCamera.stopPreview();
            return;
        }
        if (enable) {
            mCamera.startPreview();
        } else {
            mCamera.stopPreview();
        }
    }
}