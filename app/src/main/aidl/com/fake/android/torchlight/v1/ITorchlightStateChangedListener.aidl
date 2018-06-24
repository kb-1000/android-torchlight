// ITorchlightStateChangedListener.aidl
package com.fake.android.torchlight.v1;

// Declare any non-default types here with import statements

interface ITorchlightStateChangedListener {
    /**
     * This method will be called on your listener, once you register it on the {@link ITorchlight}.
     *
     * @param newState contains the new {@link ITorchlight} state
     */
    void onTorchlightChanged(boolean newState);
}
