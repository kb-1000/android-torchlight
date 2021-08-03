package de.kb1000.flashlight.core

import android.content.Context
import android.os.RemoteException
import de.kb1000.flashlight.v1.IFlashlight
import de.kb1000.flashlight.v1.IFlashlightStateChangedListener
import org.jetbrains.annotations.Contract
import timber.log.Timber
import java.util.*

abstract class Flashlight : IFlashlight.Stub() {
    private val releaseLock = Any()
    protected lateinit var context: Context
    private var enabled = false
    private var refs = 0
    private var released = false
    private val listeners = ArrayList<IFlashlightStateChangedListener>()

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

    override fun retain(): IFlashlight {
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
        if (FlashlightControl.hasFlash() || this is FlashlightFallback) {
            // TODO: can't this if statement be inlined when invalid {@link de.kb1000.flashlight.v1.IFlashlight}s aren't exposed anymore?
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
        return FlashlightControl.hasFlash()
    }

    protected fun rawSet(state: Boolean) {
        enabled = state
        for (listener in listeners) {
            try {
                listener.onFlashlightChanged(state)
            } catch (e: RemoteException) {
                Timber.w(e, "IFlashlightStateChangedListener disconnected, removing")
                listeners.remove(listener)
            }

        }
    }

    override fun addStateChangedListener(listener: IFlashlightStateChangedListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        } else {
            Timber.w("IFlashlightStateChangedListener already exists")
        }
    }

    override fun removeStateChangedListenerNothrow(listener: IFlashlightStateChangedListener): Boolean {
        return if (listeners.contains(listener)) {
            listeners.remove(listener)
            true
        } else {
            false
        }
    }
}
