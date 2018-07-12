package com.fake.android.torchlight.core;

import android.content.Context;
import android.os.Build;
import org.jetbrains.annotations.Contract;

/**
 * Created by kaeptmblaubaer1000 on 16.02.2017.
 * Class to generalize Camera usage.
 */
public class TorchlightControl {

    private static boolean _hasFlash = true;

    private static Torchlight instance = null;

    public static synchronized Torchlight getInstance(Context context) {
        if (instance == null) {
            int sdk = Build.VERSION.SDK_INT;
            if (sdk > 23) {
                instance = new TorchlightMarshmallow();
            } else {
                instance = new TorchlightOld();
            }
            instance.init(context);
            instance.set(instance.get());
            if(!_hasFlash) {
                instance.release();
                instance = new TorchlightFallback();
            }
        }
        return instance;
    }

    static void noFlash() {
        _hasFlash = false;
    }

    @Contract(pure = true)
    public static boolean hasFlash() {
        return _hasFlash;
    }

    public static boolean isEnabled(Context context) {
        return getInstance(context).get();
    }

}
