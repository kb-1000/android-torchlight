package com.fake.android.torchlight.camera;

import com.fake.android.torchlight.v1.ITorchlight;
import org.jetbrains.annotations.Contract;

// TODO: Merge with {@link Camera}
public final class TorchlightImpl extends ITorchlight.Stub implements ITorchlight {
    private Camera torchlight;

    public TorchlightImpl(Camera torchlight) {
        this.torchlight = torchlight;
    }

    /**
     * @return the current enabled state
     */
    @Override
    public boolean get() {
        return torchlight.get();
    }

    /**
     * @param state the enabled state to set
     */
    @Override
    public void set(boolean state) {
        torchlight.set(state);
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
        return CameraControl.hasFlash();
    }
}
