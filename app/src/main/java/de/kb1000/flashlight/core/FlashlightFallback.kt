package de.kb1000.flashlight.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import de.kb1000.flashlight.Common
import de.kb1000.flashlight.R
import de.kb1000.flashlight.v1.IFlashlightStateChangedListener
import timber.log.Timber

class FlashlightFallback : Flashlight() {
    override fun init(context: Context) {}

    override fun _release() {}

    override fun _set(enable: Boolean) {
        if (enable) {
            context.startActivity(Intent(context, Activity::class.java))
        }

        // The disable part can be found below, in the Activity.
    }

    /**
     * This [android.app.Activity] provides compatibility for devices without a flash.
     */
    class Activity : AppCompatActivity() {
        private var flashlightFallback: FlashlightFallback? = null

        private val listener: IFlashlightStateChangedListener = object : IFlashlightStateChangedListener.Stub() {
            override fun onFlashlightChanged(newState: Boolean) {
                if (!newState) {
                    finish()
                }
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_flash)
            val flashlight = Common.blockingFlashlightBind(this)
            if (flashlight !is FlashlightFallback) {
                val msg = "FlashlightFallback\$Activity is only allowed to be called if the IFlashlight instance is a FlashlightFallback"
                Timber.e(msg)
                Common.toast(this, msg, Toast.LENGTH_LONG)
                finish()
                return
            }
            flashlightFallback = flashlight
            flashlight.addStateChangedListener(listener)
        }

        override fun onResume() {
            super.onResume()
            flashlightFallback?.rawSet(true)
        }

        override fun onPause() {
            super.onPause()
            flashlightFallback?.rawSet(false)
        }

        override fun onDestroy() {
            super.onDestroy()
            flashlightFallback?.removeStateChangedListenerNothrow(listener)
        }
    }
}