package com.fake.android.torchlight.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.widget.Toast;
import com.fake.android.torchlight.R;

@TargetApi(Build.VERSION_CODES.M)
class TorchlightMarshmallow extends Torchlight {
    private String mCameraID;
    private CameraManager mCameraManager;
    private boolean invalid = false;
    private String invalidityErrorString;


    public void init(Context context) {
        this.context = context;
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            assert mCameraManager != null;
            String[] list = mCameraManager.getCameraIdList();
            mCameraID = list[0];
        } catch (CameraAccessException e) {
            TorchlightControl.noFlash();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            invalid = true;
            invalidityErrorString = e.getMessage();
            TorchlightControl.noFlash();
        } catch (IndexOutOfBoundsException e) {
            Toast.makeText(context, R.string.no_camera, Toast.LENGTH_LONG).show();
            invalid = true;
            invalidityErrorString = context.getString(R.string.no_camera);
            TorchlightControl.noFlash();
        }
    }

    @Override
    public void _release() {
    }

    protected void _set(boolean enable) {
        if (invalid) {
            Toast.makeText(context, invalidityErrorString, Toast.LENGTH_LONG).show();
            TorchlightControl.noFlash();
            return;
        }
        try {
            mCameraManager.setTorchMode(mCameraID, enable);
        } catch (CameraAccessException e) {
            TorchlightControl.noFlash();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
