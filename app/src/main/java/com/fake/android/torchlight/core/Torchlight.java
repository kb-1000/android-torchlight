package com.fake.android.torchlight.core;

import android.content.Context;
import android.os.RemoteException;
import com.fake.android.torchlight.v1.ITorchlight;
import com.fake.android.torchlight.v1.ITorchlightStateChangedListener;
import org.jetbrains.annotations.Contract;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;

public abstract class Torchlight extends ITorchlight.Stub {
    private final Object releaseLock = new Object();
    protected Context context;
    private boolean enabled = false;
    private int refs = 0;
    private boolean released = false;
    private List<ITorchlightStateChangedListener> listeners = new ArrayList<>();

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
        if (TorchlightControl.hasFlash()) { //TODO: include || (this instanceof TorchlightFallback)
            // TODO: can't this if statement be inlined when invalid {@link com.fake.android.torchlight.v1.ITorchlight}s aren't exposed anymore?
            rawSet(state);
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

    protected void rawSet(boolean state) {
        enabled = state;
        for (ITorchlightStateChangedListener listener : listeners) {
            try {
                listener.onTorchlightChanged(state);
            } catch (RemoteException e) {
                Timber.w(e, "ITorchlightStateChangedListener disconnected, removing");
                listeners.remove(listener);
            }
        }
    }

    public void addStateChangedListener(ITorchlightStateChangedListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        } else {
            Timber.w("ITorchlightStateChangedListener already exists");
        }
    }

    public boolean removeStateChangedListenerNothrow(ITorchlightStateChangedListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
            return true;
        } else {
            return false;
        }
    }
}
