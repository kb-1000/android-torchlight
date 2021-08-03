// IFlashlight.aidl
package de.kb1000.flashlight.v1;

import de.kb1000.flashlight.v1.IFlashlightStateChangedListener;

/**
 * This isn't meant to be used by other apps to access this app for now, but that may be supported in future.
 */
interface IFlashlight {
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
    IFlashlight retain();

    /**
     * Decrease the internal reference count of this object.
     * If this is zero and {@link .get} returns {@code false}, the object is low-level released and initialized again when using {@link #retain}, {@link #set} or {@link #toggle}.
     */
    void release();



    void addStateChangedListener(IFlashlightStateChangedListener listener);

    boolean removeStateChangedListenerNothrow(IFlashlightStateChangedListener listener);
}
