package com.fake.android.torchlight.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.widget.Toast;

import com.fake.android.torchlight.R;

class TorchlightMarshmallow extends Torchlight {
    private String mCameraID;
    private CameraManager mCameraManager;
    private Context mContext;
    private boolean invalid = false;
    private String invalidityErrorString;


    @TargetApi(Build.VERSION_CODES.M)
    public void init(Context context) {
        mContext = context;
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);

        try {
            assert mCameraManager != null;
            String[] list = mCameraManager.getCameraIdList();
            mCameraID = list[0];
        } catch (CameraAccessException e) {
            TorchlightControl.noFlash();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
            invalid = true;
            invalidityErrorString = e.getMessage();
            TorchlightControl.noFlash();
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(mContext, R.string.no_camera, Toast.LENGTH_LONG).show();
            invalid = true;
            invalidityErrorString = mContext.getString(R.string.no_camera);
            TorchlightControl.noFlash();
        }
    }

    public void release() {
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void _set(boolean enable) {
        if (invalid) {
            Toast.makeText(mContext, invalidityErrorString, Toast.LENGTH_LONG).show();
            TorchlightControl.noFlash();
            return;
        }
        try {
            mCameraManager.setTorchMode(mCameraID, enable);
        } catch (CameraAccessException e) {
            TorchlightControl.noFlash();
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}