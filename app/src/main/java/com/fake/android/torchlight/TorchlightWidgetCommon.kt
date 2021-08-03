package com.fake.android.torchlight

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.RemoteException
import android.widget.RemoteViews
import timber.log.Timber

/**
 * Created by kaeptmblaubaer1000 on 18.05.2017.
 * This is used to update the text of the widget.
 */

internal object TorchlightWidgetCommon {

    @SuppressLint("PrivateResource")
    private fun updateOne(context: Context, appWidgetManager: AppWidgetManager,
                          appWidgetId: Int) {

        val camera = Common.blockingTorchlightBind(context)
        val enabled: Boolean
        try {
            enabled = camera.get()
        } catch (e: RemoteException) {
            Timber.e(e)
            throw RuntimeException(e)
        }

        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.torchlight_widget)

        views.setTextViewText(R.id.appwidget_text, context.getString(if (enabled) androidx.appcompat.R.string.abc_capital_on else androidx.appcompat.R.string.abc_capital_off))
        views.setContentDescription(R.id.appwidget_text, context.getString(if (enabled) androidx.appcompat.R.string.abc_capital_on else androidx.appcompat.R.string.abc_capital_off))
        views.setImageViewResource(R.id.imageButton, if (enabled) R.drawable.ic_sunny_white else R.drawable.ic_sunny_black)
        views.setContentDescription(R.id.imageButton, context.getString(if (enabled) R.string.torchlight_is_on else R.string.torchlight_is_off))
        views.setOnClickPendingIntent(R.id.imageButton, PendingIntent.getBroadcast(context, 0, Intent(context, ToggleReceiver::class.java), 0))

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun update(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateOne(context, appWidgetManager, appWidgetId)
        }
    }
}
