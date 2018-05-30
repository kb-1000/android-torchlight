package com.fake.android.torchlight;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import com.fake.android.torchlight.v1.ITorchlight;
import timber.log.Timber;

public class ToggleReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        ITorchlight camera = Common.blockingTorchlightBind(context);
        try {
            camera.toggle();
        } catch (RemoteException e) {
            Timber.e(e);
            throw new RuntimeException(e);
        }
        TorchlightWidgetCommon.update(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, TorchlightWidget.class)));
    }
}
