package com.fake.android.torchlight.camera;

import android.content.Context;
import android.os.Build;

/**
 * Created by admin on 16.02.2017.
 * Class to generalize Camera usage.
 */
public class CameraControl {

    private static boolean _hasFlash = true;

    private static Camera instance = null;

    public static Camera getInstance(Context context) {
        if (instance == null) {
            int sdk = Build.VERSION.SDK_INT;
            if (sdk > 23) {
                instance = new CameraMarshmallow();
            } else {
                instance = new CameraNormal();
            }
            instance.init(context);
        }
        return instance;
    }

    static void noFlash() {
        _hasFlash = false;
    }

    public static boolean hasFlash() {
        return _hasFlash;
    }

    public static boolean isEnabled(Context context) {
        return getInstance(context).get();
    }

}
