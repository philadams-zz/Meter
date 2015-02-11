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
public class NotificationNrsReceiver extends BroadcastReceiver {

  private final String TAG = getClass().getSimpleName();

  @Override
  public void onReceive(Context context, Intent intent) {

    // grab reported pain value
    int reportedValue = intent.getIntExtra(Constants.NOTIFY_ACTION_PAIN_VALUE, -1);
    Log.d(TAG, String.format("reported value: %d", reportedValue));

    // we don't report to Ohmage in this demo as there's no 'push' interface
    // TODO:philadams store reportedValue

    // grab notification manager
    final NotificationManager mNotificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    // from a UX perspective, what we really want to do is update the notification to highlight
    // the selected value, and then after 1 second (or so) cancel the notification...
    Notification.Builder mBuilder = getNrsBuilder(context, reportedValue);
    mNotificationManager.notify(Constants.NOTIFY_NRS, mBuilder.build());
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override public void run() {
        mNotificationManager.cancel(Constants.NOTIFY_NRS);
      }
    }, Constants.WAIT_TO_CANCEL_NOTIFICATION);
  }

  public static Notification.Builder getNrsBuilder(Context context, int nToHighlight) {
    Intent resultIntent = new Intent(context, SuperVASNumberedActivity.class);
    PendingIntent resultPendingIntent =
        PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    RemoteViews notificationView =
        new RemoteViews(context.getPackageName(), R.layout.notification_action_nrs);

    final int[] reportableValues = {
        R.id.pain_0, R.id.pain_1, R.id.pain_2, R.id.pain_3, R.id.pain_4, R.id.pain_5, R.id.pain_6,
        R.id.pain_7, R.id.pain_8, R.id.pain_9, R.id.pain_10
    };
    for (int i = 0; i < reportableValues.length; i++) {
      Intent mIntent = new Intent(context, NotificationNrsReceiver.class);
      mIntent.putExtra(Constants.NOTIFY_ACTION_PAIN_VALUE, i);
      PendingIntent pendingIntent =
          PendingIntent.getBroadcast(context, i, mIntent, PendingIntent.FLAG_ONE_SHOT);
      if (i == nToHighlight) {
        notificationView.setTextColor(reportableValues[i], Color.RED);  // highlight the selected view
      } else {
        notificationView.setTextColor(reportableValues[i], Color.BLACK);
      }
      notificationView.setOnClickPendingIntent(reportableValues[i], pendingIntent);
    }

    Notification.Builder mBuilder =
        new Notification.Builder(context).setSmallIcon(R.drawable.ic_launcher)
            .setContent(notificationView)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true);

    return mBuilder;
  }
}
