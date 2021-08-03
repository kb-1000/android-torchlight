package de.kb1000.flashlight.core

import android.annotation.TargetApi
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import de.kb1000.flashlight.R
import timber.log.Timber

@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(Build.VERSION_CODES.M)
internal class FlashlightMarshmallow : Flashlight() {
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
            FlashlightControl.noFlash()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            invalid = true
            invalidityErrorString = e.message
            FlashlightControl.noFlash()
        } catch (e: IndexOutOfBoundsException) {
            Toast.makeText(context, R.string.no_camera, Toast.LENGTH_LONG).show()
            invalid = true
            invalidityErrorString = context.getString(R.string.no_camera)
            FlashlightControl.noFlash()
        }

    }

    override fun _release() {}

    override fun _set(enable: Boolean) {
        if (invalid) {
            Toast.makeText(context, invalidityErrorString, Toast.LENGTH_LONG).show()
            FlashlightControl.noFlash()
            return
        }
        try {
            mCameraManager!!.setTorchMode(mCameraID!!, enable)
        } catch (e: CameraAccessException) {
            FlashlightControl.noFlash()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            Timber.e(e, "CameraManager.setTorchMode failed")
        } catch (e: IllegalArgumentException) {
            FlashlightControl.noFlash()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            Timber.e(e, "CameraManager.setTorchMode failed")
        }

    }
}
