// IFlashlightStateChangedListener.aidl
package de.kb1000.flashlight.v1;

// Declare any non-default types here with import statements

interface IFlashlightStateChangedListener {
    /**
     * This method will be called on your listener, once you register it on the {@link IFlashlight}.
     *
     * @param newState contains the new {@link IFlashlight} state
     */
    void onFlashlightChanged(boolean newState);
}
