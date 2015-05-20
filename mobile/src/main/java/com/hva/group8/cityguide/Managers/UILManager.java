package com.hva.group8.cityguide.Managers;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import com.hva.group8.cityguide.R;

/**
 * Created by Lansenou on 19-5-2015.
 */
public class UILManager {

    private static UILManager _instance;

    public static UILManager instance() {
        if (_instance == null)
            _instance = new UILManager();
        return _instance;
    }

    public ImageLoader getImageLoader(Context context) {
        if (!ImageLoader.getInstance().isInited()) {
            //Default Display Configuration
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)            //SET THE IMAGE CONFIG SIZE
                    .imageScaleType(ImageScaleType.EXACTLY)         //SET THE IMAGE MAX SIZE
                    .showImageForEmptyUri(R.drawable.no_image)      //NO IMAGE FOUND
                    .showImageOnFail(R.drawable.no_image)           //IMAGE FAILED
                    .showImageOnLoading(R.drawable.load_image)      //WHILE LOADING
                    .build();                                       //Build the Image Config

            //Default Image Configuration
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .defaultDisplayImageOptions(defaultOptions)     //Give the Display Options to the config
                    .diskCacheExtraOptions(75, 75, null)            //Disk Image Max Size
                    .memoryCacheExtraOptions(1200, 800)             //Cache Image Max Size
                    .build();                                       //Build the Loader Config
            ImageLoader.getInstance().init(config);                 //Set Our Loader as THE ImageLoader
        }
        return ImageLoader.getInstance();
    }

    public DisplayImageOptions getRoundImage() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)            //SET THE IMAGE CONFIG SIZE
                .imageScaleType(ImageScaleType.EXACTLY)         //SET THE IMAGE MAX SIZE
                .showImageForEmptyUri(R.drawable.no_image)      //NO IMAGE FOUND
                .showImageOnFail(R.drawable.no_image)           //IMAGE FAILED
                .showImageOnLoading(R.drawable.load_image)      //WHILE LOADING
                .displayer(new RoundedBitmapDisplayer(100))     //ROUND THE IMAGE
                .build();                                       //Build the Image Config
    }

    public DisplayImageOptions getNormalImage() {
        //Display Configuration
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)            //SET THE IMAGE CONFIG SIZE
                .imageScaleType(ImageScaleType.EXACTLY)         //SET THE IMAGE MAX SIZE
                .showImageForEmptyUri(R.drawable.no_image)      //NO IMAGE FOUND
                .showImageOnFail(R.drawable.no_image)           //IMAGE FAILED
                .showImageOnLoading(R.drawable.load_image)      //WHILE LOADING
                .build();                                       //Build the Image Config
    }
}

