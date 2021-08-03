package de.kb1000.flashlight

import android.app.Service
import android.content.Intent
import android.os.IBinder
import de.kb1000.flashlight.core.Flashlight
import de.kb1000.flashlight.core.FlashlightControl

class FlashlightService : Service() {
    private var flashlight: Flashlight? = null

    private val impl: Flashlight
        @Synchronized get() = if (flashlight != null) {
            flashlight!!
        } else {
            flashlight = FlashlightControl.getInstance(this)
            flashlight!!
        }

    override fun onBind(intent: Intent): IBinder {
        return impl
    }
}
