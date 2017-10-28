package com.fake.android.torchlight;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.fake.android.torchlight.camera.Camera;
import com.fake.android.torchlight.camera.CameraControl;

public class ToggleReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Camera camera = CameraControl.getInstance(context);
        if (CameraControl.isEnabled(context)) {
            camera.set(false);
        } else {
            camera.set(true);
        }
        TorchlightWidgetCommon.update(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, TorchlightWidget.class)));
    }
}
