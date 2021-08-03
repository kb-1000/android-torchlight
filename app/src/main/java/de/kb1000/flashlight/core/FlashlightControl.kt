package de.kb1000.flashlight.core

import android.content.Context
import android.os.Build
import org.jetbrains.annotations.Contract

/**
 * Created by kb1000 on 16.02.2017.
 * Class to generalize Camera usage.
 */
object FlashlightControl {

    private var _hasFlash = true

    private var instance: Flashlight? = null

    @Synchronized
    fun getInstance(context: Context): Flashlight {
        if (instance == null) {
            instance = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                FlashlightMarshmallow()
            } else {
                FlashlightOld()
            }
            instance!!.init(context)
            instance!!.set(instance!!.get())
            if (!_hasFlash) {
                instance!!.release()
                instance = FlashlightFallback()
            }
        }
        return instance!!
    }

    internal fun noFlash() {
        _hasFlash = false
    }

    @Contract(pure = true)
    fun hasFlash(): Boolean {
        return _hasFlash
    }

    fun isEnabled(context: Context): Boolean {
        return getInstance(context).get()
    }

}
