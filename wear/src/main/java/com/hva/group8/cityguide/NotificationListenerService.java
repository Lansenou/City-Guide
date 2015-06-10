package com.hva.group8.cityguide;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.PowerManager;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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


        //TODO watch interface should close when app closes
    }

    public void MakeNotificationMain(String text, String img, int imgId) {
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

    public void MakeNotificationMain(String text, Bitmap background, Bitmap maneuver) {

        String direction = text.replace("/direction:", "");
        Log.e("Text: ", direction);

        //Notification Activity / Intent
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        notificationIntent.putExtra(NotificationActivity.EXTRA_TITLE, direction);
        notificationIntent.putExtra(NotificationActivity.EXTRA_IMAGE, background);
        notificationIntent.putExtra(NotificationActivity.EXTRA_MANEUVER, maneuver);

        //Pending intent from notification intent
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        //Notifcation Builder
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.appicon)
                .setContentText(Html.fromHtml(direction))
                .extend(new NotificationCompat.WearableExtender()
                                .setCustomSizePreset(Notification.WearableExtender.SIZE_LARGE)
                                .setDisplayIntent(notificationPendingIntent)
                                .setBackground(background)
                );

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(100, notificationBuilder.build());
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
            if (event.getType() == DataEvent.TYPE_CHANGED &&
                    event.getDataItem().getUri().getPath().contains("/direction")) {

                PowerManager.WakeLock mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Clock");
                mWakeLock.acquire(10000);
                DataItem dataItem = event.getDataItem();
                Log.i("DataItem", dataItem.toString());

                //DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);

                DataItemAsset assetBackground = dataItem.getAssets().get("BACKGROUND");
                DataItemAsset assetManeuver = dataItem.getAssets().get("MANEUVER");

                Bitmap background = loadBitmapFromAsset(assetBackground);
                Bitmap maneuver = loadBitmapFromAsset(assetManeuver);

                String path = event.getDataItem().getUri().getPath();
                MakeNotificationMain(path, background, maneuver);

                Log.e("Adding content", path);
            }
        }
    }

    public Bitmap loadBitmapFromAsset(DataItemAsset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result =
                mGoogleApiClient.blockingConnect(200, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                mGoogleApiClient, asset).await().getInputStream();
        mGoogleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        // decode the stream into a bitmap
        return BitmapFactory.decodeStream(assetInputStream);
    }

    public Bitmap loadBitmapFromAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result =
                mGoogleApiClient.blockingConnect(200, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                mGoogleApiClient, asset).await().getInputStream();
        mGoogleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        // decode the stream into a bitmap
        return BitmapFactory.decodeStream(assetInputStream);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(PATH_DISMISS)) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(NOTIFICATION_ID);

        } //else if (messageEvent.getPath().equals(PATH))
        Log.e("Path", messageEvent.getPath());
    }
}
