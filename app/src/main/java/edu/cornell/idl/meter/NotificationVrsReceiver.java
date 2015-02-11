package edu.cornell.idl.meter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Handle Notification actions
 */
public class NotificationVrsReceiver extends BroadcastReceiver {

  private final String TAG = getClass().getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {

    // grab reported pain value
    int reportedValue = intent.getIntExtra(Constants.NOTIFY_ACTION_PAIN_VALUE, -1);
    Log.d(TAG, String.format("reported value: %d", reportedValue));

    // we don't report to Ohmage in this demo as there's no 'push' interface
    // TODO:philadams store reportedValue

    // grab notification manager and cancel notification
    final NotificationManager mNotificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.cancel(Constants.NOTIFY_VRS);
  }

}
