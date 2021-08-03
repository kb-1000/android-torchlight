package de.kb1000.flashlight

import android.os.Bundle
import android.os.RemoteException
import androidx.appcompat.app.AppCompatActivity
import de.kb1000.flashlight.databinding.ActivityAutoFlashBinding
import de.kb1000.flashlight.v1.IFlashlight
import timber.log.Timber

class AutoFlashActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAutoFlashBinding
    private lateinit var flashlight: IFlashlight

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutoFlashBinding.inflate(layoutInflater)
        binding.finish.setOnClickListener { finish() }
        val view = binding.root
        setContentView(view)

        try {
            flashlight = Common.blockingFlashlightBind(this).retain()
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

    }

    override fun onResume() {
        super.onResume()
        try {
            flashlight.set(true)
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

    }

    override fun onPause() {
        super.onPause()
        try {
            flashlight.set(false)
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

    }

    override fun finish() {
        super.finish()
        try {
            flashlight.release()
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

    }
}
