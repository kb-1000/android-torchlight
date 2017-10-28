package com.fake.android.torchlight.camera;

import android.content.Context;

public abstract class Camera {
    private boolean enabled = false;

    abstract public void init(Context context);

    abstract public void release();

    protected abstract void toggle(boolean enable);

    public boolean get() {
        return enabled;
    }

    public void set(boolean enabled) {
        this.enabled = enabled;
        this.toggle(enabled);
    }
}