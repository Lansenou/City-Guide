package com.hva.group8.cityguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mustafa on 17-3-2015.
 */

public class NotificationActivity extends Activity {
    private static final String TAG = NotificationActivity.class.getSimpleName();

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_IMAGE = "image";
    public static final String EXTRA_MANEUVER = "maneuver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        Log.e("NotificationActivity", "WTF");

        Intent intent = getIntent();

        //Text
        TextView mTextView = (TextView) findViewById(R.id.text_view);
        String title = intent.getStringExtra(EXTRA_TITLE);
        mTextView.setText(Html.fromHtml(title));

        //Background
        //ImageView background = (ImageView) findViewById(R.id.background);
        //Bitmap backgroundImage = intent.getParcelableExtra(EXTRA_IMAGE);
        //background.setImageBitmap(backgroundImage);

        //Maneuver
        ImageView maneuver = (ImageView) findViewById(R.id.image_view);
        Bitmap maneuverImage = intent.getParcelableExtra(EXTRA_MANEUVER);
        maneuver.setImageBitmap(maneuverImage);
    }
}

