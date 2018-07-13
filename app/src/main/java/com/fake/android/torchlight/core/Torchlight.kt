package com.fake.android.torchlight.core

import android.content.Context
import android.os.RemoteException
import com.fake.android.torchlight.v1.ITorchlight
import com.fake.android.torchlight.v1.ITorchlightStateChangedListener
import org.jetbrains.annotations.Contract
import timber.log.Timber
import java.util.*

abstract class Torchlight : ITorchlight.Stub() {
    private val releaseLock = Any()
    protected lateinit var context: Context
    private var enabled = false
    private var refs = 0
    private var released = false
    private val listeners = ArrayList<ITorchlightStateChangedListener>()

    abstract fun init(context: Context)

    abstract fun _release()

    override fun release() {
        refs -= 1
        releaseUnreferenced()
    }

    private fun releaseUnreferenced() {
        synchronized(releaseLock) {
            if (refs <= 0 && !enabled && !released) {
                _release()
                released = true
            }
        }
    }

    override fun retain(): ITorchlight {
        refs += 1
        initUninitialized()
        return this
    }

    private fun initUninitialized() {
        synchronized(releaseLock) {
            if (released) {
                init(context)
                set(enabled)
            }
        }
    }

    protected abstract fun _set(enable: Boolean)

    /**
     * @return the current enabled state
     */
    override fun get(): Boolean {
        return enabled
    }

    /**
     * @param state the enabled state to set
     */
    override fun set(state: Boolean) {
        initUninitialized()
        this._set(state)
        if (TorchlightControl.hasFlash() || this is TorchlightFallback) {
            // TODO: can't this if statement be inlined when invalid {@link com.fake.android.torchlight.v1.ITorchlight}s aren't exposed anymore?
            rawSet(state)
        }

        releaseUnreferenced()
    }

    /**
     * If the flash is on, this toggles it off and if the flash is off this toggles it on.
     *
     * @return the current state
     */
    override fun toggle(): Boolean {
        set(!get())
        return get()
    }

    /**
     * @return a flag if the app has detected that there is no flash in the camera
     *
     *
     * This may be false-positive if the flash was never turned on!
     */
    @Contract(pure = true)
    override fun hasFlash(): Boolean {
        return TorchlightControl.hasFlash()
    }

    protected fun rawSet(state: Boolean) {
        enabled = state
        for (listener in listeners) {
            try {
                listener.onTorchlightChanged(state)
            } catch (e: RemoteException) {
                Timber.w(e, "ITorchlightStateChangedListener disconnected, removing")
                listeners.remove(listener)
            }

        }
    }

    override fun addStateChangedListener(listener: ITorchlightStateChangedListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        } else {
            Timber.w("ITorchlightStateChangedListener already exists")
        }
    }

    override fun removeStateChangedListenerNothrow(listener: ITorchlightStateChangedListener): Boolean {
        return if (listeners.contains(listener)) {
            listeners.remove(listener)
            true
        } else {
            false
        }
    }
}
