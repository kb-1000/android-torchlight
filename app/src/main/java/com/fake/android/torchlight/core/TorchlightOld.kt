package com.fake.android.torchlight.core

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Camera
import android.widget.Toast

@SuppressLint("deprecation")
internal class TorchlightOld : Torchlight() {
    private var mCamera: Camera? = null
    private var mCameraParams: Camera.Parameters? = null

    override fun init(context: Context) {
        this.context = context
        try {
            mCamera = Camera.open()
            mCameraParams = mCamera!!.parameters
        } catch (e: RuntimeException) {
            TorchlightControl.noFlash()
            Toast.makeText(this.context, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun _release() {
        try {
            if (mCamera != null) {
                mCamera!!.release()
                mCamera = null
                mCameraParams = null
            }
        } catch (e: RuntimeException) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

    }

    public override fun _set(enable: Boolean) {
        if (mCamera == null) {
            return
        }
        if (enable) {
            mCameraParams!!.flashMode = "torch"
        } else {
            mCameraParams!!.flashMode = "off"
        }
        try {
            mCamera!!.parameters = mCameraParams
        } catch (e: RuntimeException) {
            if (e.message?.startsWith("set") == true) {
                TorchlightControl.noFlash()
            }
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            mCamera!!.stopPreview()
            return
        }

        if (enable) {
            mCamera!!.startPreview()
        } else {
            mCamera!!.stopPreview()
        }
    }
}
