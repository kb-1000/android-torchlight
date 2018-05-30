// ITorchlight.aidl
package com.fake.android.torchlight.v1;

/**
 * This isn't meant to be used by other apps to access this app, but it may be done in the future.
 */
interface ITorchlight {
    /**
     * @return the current enabled state
     */
    boolean get();

    /**
     * @param state the enabled state to set
     */
    void set(boolean state);

    /**
     * If the flash is on, this toggles it off and if the flash is off this toggles it on.
     *
     * @return the current state
     */
    boolean toggle();

    /**
     * @return a flag if the app has detected that there is no flash in the camera
     * <p>
     * This may be wrong if the flash was never turned on!
     */
    boolean hasFlash();

    /**
     * Increase the internal reference count of this object.
     */
    ITorchlight retain();

    /**
     * Decrease the internal reference count of this object.
     * If this is zero and {@link .get} returns {@code false}, the object is low-level released and initialized again when using {@link #retain}, {@link #set} or {@link #toggle}.
     */
    void release();
}
