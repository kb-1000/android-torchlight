package com.fake.android.torchlight

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.fake.android.torchlight.core.Torchlight
import com.fake.android.torchlight.core.TorchlightControl

class TorchlightService : Service() {
    private var torchlight: Torchlight? = null

    private val impl: Torchlight
        @Synchronized get() = if (torchlight != null) {
            torchlight!!
        } else {
            torchlight = TorchlightControl.getInstance(this)
            torchlight!!
        }

    override fun onBind(intent: Intent): IBinder? {
        return impl
    }
}
