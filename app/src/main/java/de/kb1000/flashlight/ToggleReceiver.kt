package de.kb1000.flashlight

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.RemoteException
import timber.log.Timber

class ToggleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val camera = Common.blockingFlashlightBind(context)
        try {
            camera.toggle()
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

        FlashlightWidgetCommon.update(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, FlashlightWidget::class.java)))
    }
}
