/*
 * Copyright (c) 2015.
 * Created by Lansenou on 3-6-15 15:42
 *  
 */

package com.hva.group8.cityguide;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    //private static MyApp context;
    private static Context mContext;

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}