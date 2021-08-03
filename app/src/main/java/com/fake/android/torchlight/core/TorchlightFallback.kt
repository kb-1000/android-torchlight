package com.fake.android.torchlight.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fake.android.torchlight.Common
import com.fake.android.torchlight.R
import com.fake.android.torchlight.v1.ITorchlightStateChangedListener
import timber.log.Timber

class TorchlightFallback : Torchlight() {
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
        private var torchlightFallback: TorchlightFallback? = null

        private val listener: ITorchlightStateChangedListener = object : ITorchlightStateChangedListener.Stub() {
            override fun onTorchlightChanged(newState: Boolean) {
                if (!newState) {
                    finish()
                }
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_flash)
            val torchlight = Common.blockingTorchlightBind(this)
            if (torchlight !is TorchlightFallback) {
                val msg = "TorchlightFallback\$Activity is only allowed to be called if the ITorchlight instance is a TorchlightFallback"
                Timber.e(msg)
                Common.toast(this, msg, Toast.LENGTH_LONG)
                finish()
                return
            }
            torchlightFallback = torchlight
            torchlight.addStateChangedListener(listener)
        }

        override fun onResume() {
            super.onResume()
            torchlightFallback?.rawSet(true)
        }

        override fun onPause() {
            super.onPause()
            torchlightFallback?.rawSet(false)
        }

        override fun onDestroy() {
            super.onDestroy()
            torchlightFallback?.removeStateChangedListenerNothrow(listener)
        }
    }
}