package com.fake.android.torchlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.fake.android.torchlight.camera.Camera;
import com.fake.android.torchlight.camera.CameraControl;

/**
 * Created by admin on 18.05.2017.
 * This is used to update the text of the widget.
 */

class TorchlightWidgetCommon {

    private TorchlightWidgetCommon() {
    }

    static private void updateOne(Context context, AppWidgetManager appWidgetManager,
                                  int appWidgetId) {

        Camera camera = CameraControl.getInstance(context);
        boolean enabled = camera.get();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.torchlight_widget);

        views.setTextViewText(R.id.appwidget_text, context.getString(enabled ? R.string.on : R.string.off));
        views.setContentDescription(R.id.appwidget_text, context.getString(enabled ? R.string.on : R.string.off));
        views.setImageViewResource(R.id.imageButton, enabled ? R.drawable.ic_sunny_white : R.drawable.ic_sunny_black);
        views.setContentDescription(R.id.imageButton, context.getString(enabled ? R.string.torchlight_is_on : R.string.torchlight_is_off));
        views.setOnClickPendingIntent(R.id.imageButton, PendingIntent.getBroadcast(context, 0, new Intent(context, ToggleReceiver.class), 0));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void update(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateOne(context, appWidgetManager, appWidgetId);
        }
    }
}
