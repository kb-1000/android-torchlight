package com.fake.android.torchlight.core

import android.annotation.TargetApi
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.widget.Toast
import com.fake.android.torchlight.R
import timber.log.Timber

@TargetApi(Build.VERSION_CODES.M)
internal class TorchlightMarshmallow : Torchlight() {
    private var mCameraID: String? = null
    private var mCameraManager: CameraManager? = null
    private var invalid = false
    private var invalidityErrorString: String? = null


    override fun init(context: Context) {
        this.context = context
        mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            assert(mCameraManager != null)
            val list = mCameraManager!!.cameraIdList
            mCameraID = list[0]
        } catch (e: CameraAccessException) {
            TorchlightControl.noFlash()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            invalid = true
            invalidityErrorString = e.message
            TorchlightControl.noFlash()
        } catch (e: IndexOutOfBoundsException) {
            Toast.makeText(context, R.string.no_camera, Toast.LENGTH_LONG).show()
            invalid = true
            invalidityErrorString = context.getString(R.string.no_camera)
            TorchlightControl.noFlash()
        }

    }

    override fun _release() {}

    override fun _set(enable: Boolean) {
        if (invalid) {
            Toast.makeText(context, invalidityErrorString, Toast.LENGTH_LONG).show()
            TorchlightControl.noFlash()
            return
        }
        try {
            mCameraManager!!.setTorchMode(mCameraID!!, enable)
        } catch (e: CameraAccessException) {
            TorchlightControl.noFlash()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            Timber.e(e, "CameraManager.setTorchMode failed")
        } catch (e: IllegalArgumentException) {
            TorchlightControl.noFlash()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            Timber.e(e, "CameraManager.setTorchMode failed")
        }

    }
}
