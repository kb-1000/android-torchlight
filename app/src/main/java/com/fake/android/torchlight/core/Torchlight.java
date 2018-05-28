package com.fake.android.torchlight.core;

import android.content.Context;

public abstract class Torchlight {
    private boolean enabled = false;

    abstract public void init(Context context);

    abstract public void release();

    protected abstract void _set(boolean enable);

    public boolean get() {
        return enabled;
    }

    public void set(boolean enabled) {
        this._set(enabled);
        this.enabled = enabled;
        if (!TorchlightControl.hasFlash()) {
            this.enabled = false;
        }
    }
}