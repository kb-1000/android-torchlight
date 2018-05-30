package com.fake.android.torchlight.core;

import android.content.Context;
import com.fake.android.torchlight.v1.ITorchlight;
import org.jetbrains.annotations.Contract;

public abstract class Torchlight extends ITorchlight.Stub {
    private final Object releaseLock = new Object();
    protected Context context;
    private boolean enabled = false;
    private int refs;
    private boolean released;

    abstract public void init(Context context);

    abstract public void _release();

    @Override
    public void release() {
        refs -= 1;
        releaseUnreferenced();
    }

    private void releaseUnreferenced() {
        synchronized (releaseLock) {
            if (refs <= 0 && !enabled && !released) {
                _release();
                released = true;
            }
        }
    }

    @Override
    public ITorchlight retain() {
        refs += 1;
        initUninitialized();
        return this;
    }

    private void initUninitialized() {
        synchronized (releaseLock) {
            if (released) {
                init(context);
                set(enabled);
            }
        }
    }

    protected abstract void _set(boolean enable);

    /**
     * @return the current enabled state
     */
    @Override
    public boolean get() {
        return enabled;
    }

    /**
     * @param state the enabled state to set
     */
    @Override
    public void set(boolean state) {
        initUninitialized();
        this._set(state);
        this.enabled = state;
        if (!TorchlightControl.hasFlash()) {
            this.enabled = false;
        }

        releaseUnreferenced();
    }

    /**
     * If the flash is on, this toggles it off and if the flash is off this toggles it on.
     *
     * @return the current state
     */
    @Override
    public boolean toggle() {
        set(!get());
        return get();
    }

    /**
     * @return a flag if the app has detected that there is no flash in the camera
     * <p>
     * This may be false-positive if the flash was never turned on!
     */
    @Contract(pure = true)
    @Override
    public boolean hasFlash() {
        return TorchlightControl.hasFlash();
    }
}
