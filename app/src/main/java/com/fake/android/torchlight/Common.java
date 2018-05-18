package com.fake.android.torchlight;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

final class Common {
    private static final int defaultDuration = Toast.LENGTH_SHORT;

    private Common() {
    }

    static void toast(Context context, @StringRes int resId) {
        toast(context, resId, defaultDuration);
    }

    static void toast(Context context, @StringRes int resId, int duration) {
        toast(context, context.getText(resId), duration);
    }

    static void toast(Context context, CharSequence msg) {
        toast(context, msg, defaultDuration);
    }

    static void toast(Context context, CharSequence msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }
}
