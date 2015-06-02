package com.hva.group8.cityguide;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.PowerManager;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mustafa on 18-3-2015.
 */
public class NotificationListenerService extends WearableListenerService {
    private static final String TAG = NotificationListenerService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 100;
    private PowerManager.WakeLock wl;
    private GoogleApiClient mGoogleApiClient;

    public static final String PATH_DISMISS = "/dismissnotification";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IMAGE = "image";

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "whatever");
        wl.acquire();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }


    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }

    public void MakeNotificationMain(String text, String img, int imgId) {
        ///  final String img = "right";
        // final String title = "Turn right on Kalverstraat <br /><b>in 50m</b>";

        // Build the intent to display our custom notification
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        notificationIntent.putExtra(NotificationActivity.EXTRA_TITLE, text);
        notificationIntent.putExtra(NotificationActivity.EXTRA_IMAGE, img);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle("City Guide")
                        .setOngoing(true)
                        .extend(new Notification.WearableExtender()
                                        .setCustomSizePreset(Notification.WearableExtender.SIZE_LARGE)
                                        .setDisplayIntent(notificationPendingIntent)
                                        .setBackground(BitmapFactory.decodeResource(getResources(), imgId))
                        );

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .notify(100, notificationBuilder.build());
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();

        if (!mGoogleApiClient.isConnected()) {
            ConnectionResult connectionResult = mGoogleApiClient
                    .blockingConnect(30, TimeUnit.SECONDS);
            if (!connectionResult.isSuccess()) {
                Log.e(TAG, "Service failed to connect to GoogleApiClient.");
                return;
            }
        }

        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {

                PowerManager.WakeLock mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Clock");
                mWakeLock.acquire(10000);

                String path = event.getDataItem().getUri().getPath();
                MakeNotificationMain(path, "like", R.drawable.appicon);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(PATH_DISMISS)) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }
}
