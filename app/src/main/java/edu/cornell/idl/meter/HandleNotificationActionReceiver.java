package edu.cornell.idl.meter;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Handle Notification actions
 */
public class HandleNotificationActionReceiver extends BroadcastReceiver {

  private final String TAG = getClass().getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {
    // grab reported pain value
    int reportedValue = intent.getIntExtra(Constants.NOTIFY_ACTION_PAIN_VALUE, -1);
    Log.d(TAG, String.format("reported value: %d", reportedValue));

    // we don't report to Ohmage in this demo as there's no 'push' interface
    // TODO:philadams store reportedValue

    // cancel the notification
    NotificationManager mNotificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.cancel(Constants.NOTIFY_VRS);
  }
}
