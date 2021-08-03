package com.fake.android.torchlight

import android.os.Bundle
import android.os.RemoteException
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fake.android.torchlight.v1.ITorchlight
import timber.log.Timber

class AutoFlashActivity : AppCompatActivity() {
    private var torchlight: ITorchlight? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_flash)
        try {
            torchlight = Common.blockingTorchlightBind(this).retain()
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

    }

    override fun onResume() {
        super.onResume()
        try {
            torchlight!!.set(true)
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

    }

    override fun onPause() {
        super.onPause()
        try {
            torchlight!!.set(false)
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

    }

    override fun finish() {
        super.finish()
        try {
            torchlight!!.release()
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

    }

    fun finish(view: View) {
        finish()
    }

}
