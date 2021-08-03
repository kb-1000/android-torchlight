package com.fake.android.torchlight.core

import android.content.Context
import android.os.Build
import org.jetbrains.annotations.Contract

/**
 * Created by kb1000 on 16.02.2017.
 * Class to generalize Camera usage.
 */
object TorchlightControl {

    private var _hasFlash = true

    private var instance: Torchlight? = null

    @Synchronized
    fun getInstance(context: Context): Torchlight {
        if (instance == null) {
            instance = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                TorchlightMarshmallow()
            } else {
                TorchlightOld()
            }
            instance!!.init(context)
            instance!!.set(instance!!.get())
            if (!_hasFlash) {
                instance!!.release()
                instance = TorchlightFallback()
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
