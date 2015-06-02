package com.hva.group8.cityguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

    private ImageView mImageView;
    private TextView mTextView;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        mImageView = (ImageView) findViewById(R.id.image_view);
        mTextView = (TextView) findViewById(R.id.text_view);

        Intent intent = getIntent();
        mTextView.setText(Html.fromHtml(intent.getStringExtra(EXTRA_TITLE)));

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mTextView.setTextColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            }
        });
    }

}

